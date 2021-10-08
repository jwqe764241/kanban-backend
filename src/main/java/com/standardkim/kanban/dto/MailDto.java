package com.standardkim.kanban.dto;

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
	}
}
