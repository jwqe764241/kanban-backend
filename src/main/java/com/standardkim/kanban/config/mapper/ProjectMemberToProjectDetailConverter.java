package com.standardkim.kanban.config.mapper;

import com.standardkim.kanban.dto.ProjectDto.ProjectDetail;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.ProjectMember;

import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.modelmapper.AbstractConverter;

@Component
public class ProjectMemberToProjectDetailConverter extends ModelMapperConverter<ProjectMember, ProjectDetail>{
	@Override
	protected Converter<ProjectMember, ProjectDetail> converter() {
		return new AbstractConverter<ProjectMember, ProjectDetail>() {
			@Override
			public ProjectDetail convert(ProjectMember projectMember) {
				Project project = projectMember.getProject();
				ProjectDetail projectDetail = ProjectDetail.builder()
					.id(project.getId())
					.name(project.getName())
					.description(project.getDescription())
					.registerDate(project.getRegisterDate())
					.registerUsername(project.getRegisterUser().getName())
					.build();
				return projectDetail;
			}
		};
	}
}
