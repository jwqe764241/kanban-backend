package com.standardkim.kanban.dto;

import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MailDto {
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Builder
	@AllArgsConstructor
	public static class InviteProjectMailParam {
		private String inviteeMailAddress;
		private Long projectId;
		private String projectName;
		private String inviterLogin;
		private String inviteeLogin;

		public static InviteProjectMailParam from(Project project, User inviterUser, User inviteeUser) {
			return InviteProjectMailParam.builder()
				.inviteeMailAddress(inviteeUser.getEmail())
				.projectId(project.getId())
				.projectName(project.getName())
				.inviterLogin(inviterUser.getLogin())
				.inviteeLogin(inviteeUser.getLogin())
				.build();
		}
	}
}
