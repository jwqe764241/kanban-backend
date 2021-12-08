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
			response.setHeader("Access-Control-Allow-Origin", flattenAllowedOrigins);
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
			response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PATCH, OPTION");
			response.setStatus(200);
			return;
		}

		AuthorizationHeader authorizationHeader = null;

		try {
			authorizationHeader = parseAuthorizationHeader(request);
		} catch (Exception e) {
		}

		if(authorizationHeader != null) {
			String type = authorizationHeader.getType();
			String token = authorizationHeader.getCredentials();

			if(type.equals("Bearer")){
				if(token != null && !token.isBlank() && jwtTokenProvider.isValid(token)) {
					try {
						String login = jwtTokenProvider.getLogin(token);
						User user = userService.findByLogin(login);
						SecurityUser securityUser = SecurityUser.from(user);
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
		
						if(securityUser.isEnabled()) {
							SecurityContextHolder.getContext().setAuthentication(authentication);
						}
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		filterChain.doFilter(request, response);
	}

	private AuthorizationHeader parseAuthorizationHeader(HttpServletRequest request) {
		String raw = request.getHeader("Authorization");
		if(raw == null) {
			throw new NullPointerException("Authorization is null");
		}

		String[] tokenized = raw.split(" ");

		if(tokenized.length != 2) {
			throw new IllegalArgumentException("invalid header data");
		}

		AuthorizationHeader header = AuthorizationHeader.builder()
			.type(tokenized[0])
			.credentials(tokenized[1])
			.build();

		return header;
	}
}
