package com.standardkim.kanban.global.config.handler;

import com.standardkim.kanban.domain.projectmember.application.ProjectMemberFindService;
import com.standardkim.kanban.domain.projectmember.application.ProjectRoleHierarchy;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMember;
import com.standardkim.kanban.domain.projectmember.dto.ProjectRoleName;
import com.standardkim.kanban.domain.projectmember.exception.ProjectMemberNotFoundException;
import com.standardkim.kanban.global.auth.dto.SecurityUser;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class MethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
	private final ProjectMemberFindService projectMemberFindService;
	private final ProjectRoleHierarchy projectRoleHierarchy;

	private Object filterObject;
    private Object returnObject;

	public MethodSecurityExpressionRoot(Authentication authentication, 
		ProjectMemberFindService projectMemberFindService, ProjectRoleHierarchy projectRoleHierarchy) {
		super(authentication);
		this.projectMemberFindService = projectMemberFindService;
		this.projectRoleHierarchy = projectRoleHierarchy;
	}

	private SecurityUser getSecurityUser() {
		Authentication authentication = getAuthentication();
		return (SecurityUser) authentication.getPrincipal();
	}

	public boolean hasProjectRole(Long projectId, String rawProjectRoleName) {
		SecurityUser user = getSecurityUser();
		ProjectMember projectMember = null;
		try {
			projectMember =  projectMemberFindService.findById(projectId, user.getId());
		} catch (ProjectMemberNotFoundException e) {
			return false;
		}
		
		ProjectRoleName projectRoleName = ProjectRoleName.of(rawProjectRoleName);
		if(projectRoleName.equals(ProjectRoleName.ADMIN)) {
			return projectRoleHierarchy.hasAdminRole(projectMember.getProjectRole().getName());
		} 
		else if (projectRoleName.equals(ProjectRoleName.MANAGER)) {
			return projectRoleHierarchy.hasManagerRole(projectMember.getProjectRole().getName());
		} 
		else if (projectRoleName.equals(ProjectRoleName.MEMBER)) {
			return projectRoleHierarchy.hasMemberRole(projectMember.getProjectRole().getName());
		}
		else {
			return false;
		}
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
