package com.standardkim.kanban.config.mapper;

import com.standardkim.kanban.dto.ProjectMemberDto.ProjectMemberInfo;
import com.standardkim.kanban.entity.ProjectMember;
import com.standardkim.kanban.entity.User;

import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.modelmapper.AbstractConverter;

@Component
public class ProjectMemberToProjectMemberInfo extends ModelMapperConverter<ProjectMember, ProjectMemberInfo> {
	@Override
	protected Converter<ProjectMember, ProjectMemberInfo> converter() {
		return new AbstractConverter<ProjectMember, ProjectMemberInfo>() {
			@Override
			public ProjectMemberInfo convert(ProjectMember projectMember) {
				User user = projectMember.getUser();
				ProjectMemberInfo projectMemberInfo = ProjectMemberInfo.builder()
					.id(user.getId())
					.name(user.getName())
					.email(user.getEmail())
					.date(projectMember.getRegisterDate())
					.build();
				return projectMemberInfo;
			}
		};
	}
}
