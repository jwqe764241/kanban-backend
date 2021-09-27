package com.standardkim.kanban.config.mapper;

import com.standardkim.kanban.dto.ProjectDto.ProjectInfo;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.ProjectMember;

import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.modelmapper.AbstractConverter;

@Component
public class ProjectMemberToProjectInfoConverter extends ModelMapperConverter<ProjectMember, ProjectInfo>{
	@Override
	protected Converter<ProjectMember, ProjectInfo> converter() {
		return new AbstractConverter<ProjectMember, ProjectInfo>() {
			@Override
			public ProjectInfo convert(ProjectMember projectMember) {
				Project project = projectMember.getProject();
				ProjectInfo projectInfo = ProjectInfo.builder()
					.id(project.getId())
					.name(project.getName())
					.description(project.getDescription())
					.registerDate(project.getRegisterDate())
					.registerUsername(project.getRegisterUser().getName())
					.build();
				return projectInfo;
			}
		};
	}
}
