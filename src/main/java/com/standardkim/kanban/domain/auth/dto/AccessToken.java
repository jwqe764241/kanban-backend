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
public class AccessToken {
	private String token;

	public static AccessToken from(String token) {
		return AccessToken.builder()
			.token(token)
			.build();
	}
}
