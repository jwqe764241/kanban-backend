package com.standardkim.kanban.config.handler;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.exception.project.ProjectMemberNotFoundException;
import com.standardkim.kanban.service.ProjectMemberService;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class MethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
	private final ProjectMemberService projectMemberService;

	private Object filterObject;
    private Object returnObject;

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
		try {
			return projectMemberService.isProjectOwner(projectId, user.getId());
		} catch (ProjectMemberNotFoundException e) {
			return false;
		}
	}

	public boolean isProjectMember(Long projectId) {
		SecurityUser user = getSecurityUser();
		return projectMemberService.isExist(projectId, user.getId());
	}

	@Override
	public Object getFilterObject() {
		return filterObject;
	}

	@Override
	public void setFilterObject(Object filterObject) {
		this.filterObject = filterObject;
	}

	@Override
	public Object getReturnObject() {
		return returnObject;
	}

	@Override
	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
		
	}

	@Override
	public Object getThis() {
		return this;
	}
}
