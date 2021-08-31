package com.standardkim.kanban.util;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public SecurityUser getSecurityUser() {
		Authentication authentication = getAuthentication();
		return (SecurityUser) authentication.getPrincipal();
	}
}
