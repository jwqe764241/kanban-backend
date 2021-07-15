package com.standardkim.kanban.config.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.service.AuthenticationService;
import com.standardkim.kanban.util.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
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

	private String getToken(HttpServletRequest request) {
		return request.getHeader("Authorization");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
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
