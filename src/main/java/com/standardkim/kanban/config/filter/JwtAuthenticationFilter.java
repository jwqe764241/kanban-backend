package com.standardkim.kanban.config.filter;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.service.AuthenticationService;
import com.standardkim.kanban.util.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private AuthenticationService authenticationService;

	@Value("${config.allowed-origins}")
	String[] allowedOrigins;

	private String flattenAllowedOrigins;

	@PostConstruct
	public void init() {
		flattenAllowedOrigins = String.join(",", allowedOrigins);
	}

	private String getToken(HttpServletRequest request) {
		return request.getHeader("Authorization");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//preflight 요청은 200으로 응답함.
		if(request.getMethod().equals(HttpMethod.OPTIONS.name()))
		{
			response.setHeader("Access-Control-Allow-Origin", flattenAllowedOrigins);
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
			response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PATCH, OPTION");
			response.setStatus(200);
			return;
		}

		String token = getToken(request);

		if(token != null && !token.isBlank() && jwtTokenProvider.validateToken(token)) {
			try {
				String login = jwtTokenProvider.getLogin(token);
				SecurityUser securityUser = (SecurityUser) authenticationService.loadUserByUsername(login);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());

				if(securityUser.isEnabled()) {
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}

		filterChain.doFilter(request, response);
	}
}
