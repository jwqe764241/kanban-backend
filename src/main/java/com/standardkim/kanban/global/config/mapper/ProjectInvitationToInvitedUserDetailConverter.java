package com.standardkim.kanban.global.config.mapper;

import com.standardkim.kanban.domain.projectinvitation.domain.ProjectInvitation;
import com.standardkim.kanban.domain.projectinvitation.dto.ProjectInvitationDto.InvitedUserDetail;
import com.standardkim.kanban.domain.user.domain.User;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProjectInvitationToInvitedUserDetailConverter extends ModelMapperConverter<ProjectInvitation, InvitedUserDetail> {
	@Override
	protected Converter<ProjectInvitation, InvitedUserDetail> converter() {
		return new AbstractConverter<ProjectInvitation, InvitedUserDetail>() {
			@Override
			public InvitedUserDetail convert(ProjectInvitation projectInvitation) {
				User invitedUser = projectInvitation.getInvitedUser();
				InvitedUserDetail invitedUserDetail = InvitedUserDetail.builder()
					.id(invitedUser.getId())
					.name(invitedUser.getName())
					.email(invitedUser.getEmail())
					.date(projectInvitation.getCreatedAt())
					.build();
				return invitedUserDetail;
			}
		};
	}
}
