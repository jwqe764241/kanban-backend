package com.standardkim.kanban.global.config.filter;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenFindService;
import com.standardkim.kanban.domain.refreshtoken.domain.RefreshToken;
import com.standardkim.kanban.domain.refreshtoken.exception.RefreshTokenNotFoundException;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.domain.user.exception.UserNotFoundException;
import com.standardkim.kanban.global.auth.dto.AuthorizationHeader;
import com.standardkim.kanban.global.auth.dto.SecurityUser;
import com.standardkim.kanban.global.config.property.ConfigProperties;
import com.standardkim.kanban.global.error.ErrorCode;
import com.standardkim.kanban.global.error.ErrorResponse;
import com.standardkim.kanban.global.util.ErrorResponseJsonConverter;
import com.standardkim.kanban.global.util.JwtTokenProvider;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private ErrorResponseJsonConverter errorResponseJsonConverter = new ErrorResponseJsonConverter();

	private final JwtTokenProvider jwtTokenProvider;

	private final UserFindService userFindService;

	private final RefreshTokenFindService refreshTokenFindService;

	private final ConfigProperties configProperties;

	private String flattenAllowedOrigins;

	@PostConstruct
	public void init() {
		flattenAllowedOrigins = String.join(",", configProperties.getAllowedOrigins());
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//preflight 요청은 200으로 응답함.
		if(request.getMethod().equals(HttpMethod.OPTIONS.name()))
		{
			setPreflightResponse(response);
			return;
		}

		AuthorizationHeader authorizationHeader = null;
		try {
			authorizationHeader = AuthorizationHeader.of(request.getHeader("Authorization"));
		} catch (IllegalArgumentException e) {}

		if(authorizationHeader != null){
			String token = authorizationHeader.getCredentials();
			if(authorizationHeader.isValid() && jwtTokenProvider.isValid(token)) {
				try{
					String username = jwtTokenProvider.getUsername(token);
					User user = userFindService.findByUsername(username);
					RefreshToken refreshToken = refreshTokenFindService.findById(user.getId());
					// access token and refresh token must not be same
					if(refreshToken.getToken().equals(authorizationHeader.getCredentials())) {
						setInvalidAccessTokenResponse(response);
						return;
					}
					SecurityUser securityUser = SecurityUser.of(user);
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
					if(securityUser.isEnabled()) {
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				} catch (UserNotFoundException | RefreshTokenNotFoundException e) {
					setInvalidAccessTokenResponse(response);
					return;
				} 
			}
		}
		
		filterChain.doFilter(request, response);
	}

	private void setDefaultHeader(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", flattenAllowedOrigins);
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PATCH, OPTION");
		response.setContentType("application/json;charset=UTF-8");
	}

	private void setInvalidAccessTokenResponse(HttpServletResponse response) {
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_ACCESS_TOKEN);
		String responseJson = errorResponseJsonConverter.convert(errorResponse).orElse("");
		setDefaultHeader(response);
		response.setStatus(401);
		try {
			response.getWriter().write(responseJson);
		} catch (IOException e) {}
	}

	private void setPreflightResponse(HttpServletResponse response) {
		setDefaultHeader(response);
		response.setStatus(200);
	}
}
