package com.standardkim.kanban.domain.projectmember.dto;

public enum ProjectRoleName {
	ADMIN("ADMIN"),
	MANAGER("MANAGER"),
	MEMBER("MEMBER");

	private String name;

	ProjectRoleName(String name) {
		this.name = name;
	}

	public static ProjectRoleName of(String name) {
		for(ProjectRoleName projectRoleName : values()) {
			if(projectRoleName.getName().equals(name)) {
				return projectRoleName;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}
}
