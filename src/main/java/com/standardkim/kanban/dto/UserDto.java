package com.standardkim.kanban.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.standardkim.kanban.entity.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class SecurityUser implements UserDetails {
		private Long id;
		private String login;
		private String password;
		private String name;
		private LocalDateTime registerDate;
		private Collection<? extends GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		@Builder
		public SecurityUser(Long id, String login, String password, String name, LocalDateTime registerDate) {
			this.id = id;
			this.login = login;
			this.password = password;
			this.name = name;
			this.registerDate = registerDate;
		}

		@Override
		public String getUsername() {
			return login;
		}

		@Override
		public String getPassword() {
			return password;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return authorities;
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
	}

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
			return User.builder()
				.login(login)
				.password(passwordEncoder.encode(password))
				.name(name)
				.build();
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class LoginUserRequest {
		@NotBlank
		@Size(min = 5, max = 20)
		private String login;

		@NotBlank
		@Size(min = 5, max = 20)
		private String password;
	}
}
