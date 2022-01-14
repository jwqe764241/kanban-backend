package com.standardkim.kanban.domain.auth.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class LoginParam {
	@NotBlank
	@Size(min = 5, max = 20)
	private String login;

	@NotBlank
	@Size(min = 5, max = 20)
	private String password;
}
