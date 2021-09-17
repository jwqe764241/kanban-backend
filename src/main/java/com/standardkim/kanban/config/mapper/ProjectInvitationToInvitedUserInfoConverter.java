package com.standardkim.kanban.config.mapper;

import com.standardkim.kanban.dto.ProjectInvitationDto.InvitedUserInfo;
import com.standardkim.kanban.entity.ProjectInvitation;
import com.standardkim.kanban.entity.User;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProjectInvitationToInvitedUserInfoConverter extends ModelMapperConverter<ProjectInvitation, InvitedUserInfo> {
	@Override
	protected Converter<ProjectInvitation, InvitedUserInfo> converter() {
		return new AbstractConverter<ProjectInvitation, InvitedUserInfo>() {
			@Override
			public InvitedUserInfo convert(ProjectInvitation projectInvitation) {
				User invitedUser = projectInvitation.getInvitedUser();
				InvitedUserInfo invitedUserInfo = InvitedUserInfo.builder()
					.id(invitedUser.getId())
					.name(invitedUser.getName())
					.email(invitedUser.getEmail())
					.date(projectInvitation.getRegisterDate())
					.build();
				return invitedUserInfo;
			}
		};
	}
}
