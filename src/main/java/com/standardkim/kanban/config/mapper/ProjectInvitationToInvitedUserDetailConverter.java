package com.standardkim.kanban.config.mapper;

import com.standardkim.kanban.dto.ProjectInvitationDto.InvitedUserDetail;
import com.standardkim.kanban.entity.ProjectInvitation;
import com.standardkim.kanban.entity.User;

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
					.date(projectInvitation.getRegisterDate())
					.build();
				return invitedUserDetail;
			}
		};
	}
}
