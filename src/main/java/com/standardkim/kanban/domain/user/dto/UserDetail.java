package com.standardkim.kanban.domain.user.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class UserDetail {
	private Long id;
	private String login;
	private String name;
	private String email;
	private LocalDateTime createdAt;
}
