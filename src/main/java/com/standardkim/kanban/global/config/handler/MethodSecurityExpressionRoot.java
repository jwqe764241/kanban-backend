package com.standardkim.kanban.global.config.handler;

import com.standardkim.kanban.domain.projectmember.application.ProjectMemberFindService;
import com.standardkim.kanban.domain.projectmember.exception.ProjectMemberNotFoundException;
import com.standardkim.kanban.global.auth.dto.SecurityUser;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class MethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
	private final ProjectMemberFindService projectMemberFindService;

	private Object filterObject;
    private Object returnObject;

	public MethodSecurityExpressionRoot(Authentication authentication, ProjectMemberFindService projectMemberFindService) {
		super(authentication);
		this.projectMemberFindService = projectMemberFindService;
	}

	private SecurityUser getSecurityUser() {
		Authentication authentication = getAuthentication();
		return (SecurityUser) authentication.getPrincipal();
	}

	public boolean isProjectOwner(Long projectId) {
		SecurityUser user = getSecurityUser();
		try {
			return projectMemberFindService.isProjectOwner(projectId, user.getId());
		} catch (ProjectMemberNotFoundException e) {
			return false;
		}
	}

	public boolean isProjectMember(Long projectId) {
		SecurityUser user = getSecurityUser();
		return projectMemberFindService.isExist(projectId, user.getId());
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
