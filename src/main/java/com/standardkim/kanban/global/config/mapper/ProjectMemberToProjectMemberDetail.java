package com.standardkim.kanban.global.config.mapper;

import com.standardkim.kanban.domain.projectmember.domain.ProjectMember;
import com.standardkim.kanban.domain.projectmember.presentation.ProjectMemberDto.ProjectMemberDetail;
import com.standardkim.kanban.domain.user.domain.User;

import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.modelmapper.AbstractConverter;

@Component
public class ProjectMemberToProjectMemberDetail extends ModelMapperConverter<ProjectMember, ProjectMemberDetail> {
	@Override
	protected Converter<ProjectMember, ProjectMemberDetail> converter() {
		return new AbstractConverter<ProjectMember, ProjectMemberDetail>() {
			@Override
			public ProjectMemberDetail convert(ProjectMember projectMember) {
				User user = projectMember.getUser();
				ProjectMemberDetail projectMemberDetail = ProjectMemberDetail.builder()
					.id(user.getId())
					.name(user.getName())
					.email(user.getEmail())
					.date(projectMember.getCreatedAt())
					.build();
				return projectMemberDetail;
			}
		};
	}
}
