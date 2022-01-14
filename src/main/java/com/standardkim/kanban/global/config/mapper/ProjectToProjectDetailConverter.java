package com.standardkim.kanban.global.config.mapper;

import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.project.dto.ProjectDetail;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;

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
