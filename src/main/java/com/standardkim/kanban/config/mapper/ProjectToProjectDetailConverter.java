package com.standardkim.kanban.config.mapper;

import com.standardkim.kanban.dto.ProjectDto.ProjectDetail;
import com.standardkim.kanban.entity.Project;

import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.modelmapper.AbstractConverter;

@Component
public class ProjectToProjectDetailConverter extends ModelMapperConverter<Project, ProjectDetail> {
	@Override
	protected Converter<Project, ProjectDetail> converter() {
		return new AbstractConverter<Project, ProjectDetail>() {
			@Override
			public ProjectDetail convert(Project project) {
				ProjectDetail projectDetail = ProjectDetail.builder()
					.id(project.getId())
					.name(project.getName())
					.description(project.getDescription())
					.createdAt(project.getCreatedAt())
					.registerUsername(project.getRegisterUser().getName())
					.build();
				return projectDetail;
			}
		};
	}
}
