package com.standardkim.kanban.infra.mail.dto;

import com.standardkim.kanban.domain.projectmember.domain.ProjectMember;
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
	private String inviterUsername;
	private String inviteeUsername;

	public static InviteProjectMailParam of(ProjectMember projectMember, User inviteeUser) {
		return InviteProjectMailParam.builder()
			.inviteeMailAddress(inviteeUser.getEmail())
			.projectId(projectMember.getId().getProjectId())
			.projectName(projectMember.getProject().getName())
			.inviterUsername(projectMember.getUser().getUsername())
			.inviteeUsername(inviteeUser.getUsername())
			.build();
	}
}