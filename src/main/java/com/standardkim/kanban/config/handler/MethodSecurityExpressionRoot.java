package com.standardkim.kanban.config.handler;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.service.ProjectMemberService;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class MethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
	private final ProjectMemberService projectMemberService;

	public MethodSecurityExpressionRoot(Authentication authentication, ProjectMemberService projectMemberService) {
		super(authentication);
		this.projectMemberService = projectMemberService;
	}

	private SecurityUser getSecurityUser() {
		Authentication authentication = getAuthentication();
		return (SecurityUser) authentication.getPrincipal();
	}

	public boolean isProjectOwner(Long projectId) {
		SecurityUser user = getSecurityUser();
		return projectMemberService.isProjectOwner(projectId, user.getId());
	}

	public boolean isProjectMember(Long projectId) {
		SecurityUser user = getSecurityUser();
		return projectMemberService.isMemberExists(projectId, user.getId());
	}

	@Override
	public void setFilterObject(Object filterObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getFilterObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setReturnObject(Object returnObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getReturnObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getThis() {
		// TODO Auto-generated method stub
		return null;
	}
}
