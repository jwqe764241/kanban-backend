package com.standardkim.kanban.config.mapper;

import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.ProjectMember;

import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.modelmapper.AbstractConverter;

@Component
public class ProjectMemberToProjectConverter extends ModelMapperConverter<ProjectMember, Project> {
	@Override
	protected Converter<ProjectMember, Project> converter() {
		return new AbstractConverter<ProjectMember, Project>() {
			@Override
			public Project convert(ProjectMember projectMember) {
				Project project = projectMember.getProject();
				return project;
			}
		};
	}
}
