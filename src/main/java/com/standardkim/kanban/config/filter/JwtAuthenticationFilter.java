package com.standardkim.kanban.config.filter;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.standardkim.kanban.dto.AuthenticationDto.AuthorizationHeader;
import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.user.UserNotFoundException;
import com.standardkim.kanban.service.UserService;
import com.standardkim.kanban.util.JwtTokenProvider;

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
	private final JwtTokenProvider jwtTokenProvider;

	private final UserService userService;

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
					SecurityUser securityUser = SecurityUser.from(user);
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
					if(securityUser.isEnabled()) {
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				} catch (UserNotFoundException e) {
					setUnauthorizedResponse(response);
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
	}

	private void setUnauthorizedResponse(HttpServletResponse response) {
		setDefaultHeader(response);
		response.setStatus(401);
	}

	private void setPreflightResponse(HttpServletResponse response) {
		setDefaultHeader(response);
		response.setStatus(200);
	}
}
