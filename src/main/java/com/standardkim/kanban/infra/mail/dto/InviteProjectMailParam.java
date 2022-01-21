package com.standardkim.kanban.infra.mail.dto;

import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.user.domain.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class InviteProjectMailParam {
	private String inviteeMailAddress;
	private Long projectId;
	private String projectName;
	private String inviterLogin;
	private String inviteeLogin;

	public static InviteProjectMailParam of(Project project, User inviterUser, User inviteeUser) {
		return InviteProjectMailParam.builder()
			.inviteeMailAddress(inviteeUser.getEmail())
			.projectId(project.getId())
			.projectName(project.getName())
			.inviterLogin(inviterUser.getLogin())
			.inviteeLogin(inviteeUser.getLogin())
			.build();
	}
}