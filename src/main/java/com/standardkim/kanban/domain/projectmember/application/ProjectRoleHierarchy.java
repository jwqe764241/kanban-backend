package com.standardkim.kanban.domain.projectmember.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.standardkim.kanban.domain.projectmember.dto.ProjectRoleName;

import org.springframework.stereotype.Service;

@Service
public class ProjectRoleHierarchy {
	//list contains parent role of key role
	private Map<ProjectRoleName, List<ProjectRoleName>> hierarchyMap;

	public ProjectRoleHierarchy() {
		//set to ADMIN > MANAGER > MEMBER
		hierarchyMap = new HashMap<ProjectRoleName, List<ProjectRoleName>>();
		hierarchyMap.put(ProjectRoleName.ADMIN, new ArrayList<ProjectRoleName>());

		List<ProjectRoleName> managerParentRoles = new ArrayList<ProjectRoleName>();
		managerParentRoles.add(ProjectRoleName.ADMIN);
		hierarchyMap.put(ProjectRoleName.MANAGER, managerParentRoles);

		List<ProjectRoleName> memberParentRoles = new ArrayList<ProjectRoleName>();
		memberParentRoles.add(ProjectRoleName.ADMIN);
		memberParentRoles.add(ProjectRoleName.MANAGER);
		hierarchyMap.put(ProjectRoleName.MEMBER, memberParentRoles);
	}

	//check `given` and `target` are equals, or `given` is parent role of `target`
	private boolean hasRole(ProjectRoleName target, ProjectRoleName given) {
		if(target.equals(given)) 
			return true;
		List<ProjectRoleName> parentRoles = hierarchyMap.get(target);
		return parentRoles != null ? parentRoles.contains(given) : false;
	}

	public boolean hasAdminRole(ProjectRoleName name) {
		return hasRole(ProjectRoleName.ADMIN, name);
	}

	public boolean hasManagerRole(ProjectRoleName name) {
		return hasRole(ProjectRoleName.MANAGER, name);
	}

	public boolean hasMemberRole(ProjectRoleName name) {
		return hasRole(ProjectRoleName.MEMBER, name);
	}
}
