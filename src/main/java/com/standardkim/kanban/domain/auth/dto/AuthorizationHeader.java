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
public class AuthorizationHeader { 
	private String type;
	private String credentials;

	public boolean isValid() {
		return type != null && type.equals("Bearer") &&
			credentials != null && !credentials.isBlank();
	}

	public static AuthorizationHeader from(String header) {
		if(header == null || header.isEmpty()) {
			throw new IllegalArgumentException("empty argument");
		}

		String[] tokenized = header.split(" ");

		if(tokenized.length != 2) {
			throw new IllegalArgumentException("invalid header data");
		}

		return AuthorizationHeader.builder()
			.type(tokenized[0])
			.credentials(tokenized[1])
			.build();
	}
}
