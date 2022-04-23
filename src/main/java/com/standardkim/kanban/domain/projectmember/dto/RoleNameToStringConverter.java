package com.standardkim.kanban.domain.projectmember.dto;

import javax.persistence.AttributeConverter;

public class RoleNameToStringConverter implements AttributeConverter<ProjectRoleName, String> {
	@Override
	public String convertToDatabaseColumn(ProjectRoleName projectRoleName) {
		return projectRoleName.getName();
	}

	@Override
	public ProjectRoleName convertToEntityAttribute(String name) {
		return ProjectRoleName.of(name);
	}
}
