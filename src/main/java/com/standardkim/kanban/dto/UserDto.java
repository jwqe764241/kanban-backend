package com.standardkim.kanban.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.standardkim.kanban.entity.User;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class JoinUserRequest {
		@NotBlank
		@Size(min = 5, max = 20)
		private String login;

		@NotBlank
		@Size(min = 5, max = 20)
		private String password;

		@NotBlank
		@Size(min = 2, max = 20)
		private String name;

		@Builder
		public JoinUserRequest(String login, String password, String name) {
			this.login = login;
			this.password = password;
			this.name = name;
		}

		public User toEntity(PasswordEncoder passwordEncoder) {
			return User.builder().login(login).password(passwordEncoder.encode(password)).name(name).build();
		}
	}
}
