package com.standardkim.kanban.global.config.filter;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.standardkim.kanban.domain.auth.presentation.AuthenticationDto.AuthorizationHeader;
import com.standardkim.kanban.domain.auth.presentation.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenService;
import com.standardkim.kanban.domain.refreshtoken.domain.RefreshToken;
import com.standardkim.kanban.domain.refreshtoken.exception.RefreshTokenNotFoundException;
import com.standardkim.kanban.domain.user.application.UserService;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.domain.user.exception.UserNotFoundException;
import com.standardkim.kanban.global.exception.ErrorCode;
import com.standardkim.kanban.global.exception.ErrorResponseDto.ErrorResponse;
import com.standardkim.kanban.global.util.ErrorResponseJsonConverter;
import com.standardkim.kanban.global.util.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Value;
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

	private final UserService userService;

	private final RefreshTokenService refreshTokenService;

	@Value("${config.allowed-origins}")
	String[] allowedOrigins;

	private String flattenAllowedOrigins;

	@PostConstruct
	public void init() {
		flattenAllowedOrigins = String.join(",", allowedOrigins);
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
			authorizationHeader = AuthorizationHeader.from(request.getHeader("Authorization"));
		} catch (IllegalArgumentException e) {}

		if(authorizationHeader != null){
			String token = authorizationHeader.getCredentials();
			if(authorizationHeader.isValid() && jwtTokenProvider.isValid(token)) {
				try{
					String login = jwtTokenProvider.getLogin(token);
					User user = userService.findByLogin(login);
					RefreshToken refreshToken = refreshTokenService.findById(user.getId());
					// access token and refresh token must not be same
					if(refreshToken.getToken().equals(authorizationHeader.getCredentials())) {
						setInvalidAccessTokenResponse(response);
						return;
					}
					SecurityUser securityUser = SecurityUser.from(user);
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
		ErrorResponse errorResponse = ErrorResponse.from(ErrorCode.INVALID_ACCESS_TOKEN);
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
