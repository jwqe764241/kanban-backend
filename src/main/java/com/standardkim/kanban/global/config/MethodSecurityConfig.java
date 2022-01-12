package com.standardkim.kanban.global.config;

import com.standardkim.kanban.global.config.handler.MethodSecurityExpressionHandler;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
	private final MethodSecurityExpressionHandler methodSecurityExpressionHandler;

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		return methodSecurityExpressionHandler;
	}
}
