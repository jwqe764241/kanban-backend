package com.standardkim.kanban.domain.user.dto;

import javax.validation.constraints.Email;
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
public class CreateUserParam {
	@NotBlank
	@Size(min = 5, max = 20)
	private String username;

	@NotBlank
	@Size(min = 5, max = 20)
	private String password;

	@NotBlank
	@Size(min = 2, max = 20)
	private String name;

	@NotBlank
	@Email(regexp = ".+@.+\\..+")
	private String email;
}
