package com.standardkim.kanban.global.util;

import com.standardkim.kanban.domain.auth.presentation.AuthenticationDto.SecurityUser;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextFacade {
	public static SecurityUser getSecurityUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (SecurityUser) authentication.getPrincipal();
	}
}
