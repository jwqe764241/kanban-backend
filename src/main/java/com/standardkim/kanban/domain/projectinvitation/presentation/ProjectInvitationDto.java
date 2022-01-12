package com.standardkim.kanban.domain.projectinvitation.presentation;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProjectInvitationDto {
	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class InviteProjectMemeberParam {
		@NotNull
		private Long userId;
	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class InvitedUserDetail {
		private Long id;
		private String name;
		private String email;
		private LocalDateTime date;
	}
}
