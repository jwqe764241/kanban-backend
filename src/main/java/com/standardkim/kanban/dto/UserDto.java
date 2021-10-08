package com.standardkim.kanban.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.standardkim.kanban.entity.User;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Builder
	@AllArgsConstructor
	public static class CreateUserParameter {
		@NotBlank
		@Size(min = 5, max = 20)
		private String login;

		@NotBlank
		@Size(min = 5, max = 20)
		private String password;

		@NotBlank
		@Size(min = 2, max = 20)
		private String name;

		@NotBlank
		@Size(min = 5, max = 320)
		private String email;

		public User toEntity(PasswordEncoder passwordEncoder) {
			return User.builder()
				.login(login)
				.password(passwordEncoder.encode(password))
				.name(name)
				.email(email)
				.build();
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Builder
	@AllArgsConstructor
	public static class UserDetail {
		private Long id;
		private String login;
		private String name;
		private String email;
		private LocalDateTime registerDate;
	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class SuggestionUserDetail {
		private Long id;
		private String login;
		private String name;
	}
}
