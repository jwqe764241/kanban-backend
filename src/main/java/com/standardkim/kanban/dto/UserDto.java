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

		public User toEntity(PasswordEncoder passwordEncoder) {
			return User.builder()
				.login(login)
				.password(passwordEncoder.encode(password))
				.name(name)
				.build();
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Builder
	@AllArgsConstructor
	public static class UserInfo {
		private Long id;
		private String login;
		private String name;
		private LocalDateTime registerDate;

		public UserInfo(User user) {
			this.id = user.getId();
			this.login = user.getLogin();
			this.name = user.getName();
			this.registerDate = user.getRegisterDate();
		}
	}
}
