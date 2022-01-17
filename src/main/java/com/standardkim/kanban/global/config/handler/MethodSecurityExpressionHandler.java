package com.standardkim.kanban.global.config.handler;

import com.standardkim.kanban.domain.projectmember.application.ProjectMemberService;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
	private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

	private final ProjectMemberService projectMemberService;

	@Override
	protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
		MethodSecurityExpressionRoot methodSecurityExpressionRoot = new MethodSecurityExpressionRoot(authentication, projectMemberService);
		methodSecurityExpressionRoot.setPermissionEvaluator(getPermissionEvaluator());
		methodSecurityExpressionRoot.setTrustResolver(trustResolver);
		methodSecurityExpressionRoot.setRoleHierarchy(getRoleHierarchy());
		return methodSecurityExpressionRoot;
	}
}