package com.standardkim.kanban.domain.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class AuthenticationToken {
	private String accessToken;
	private String refreshToken;

	public static AuthenticationToken of(String accessToken, String refreshToken) {
		return AuthenticationToken.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
