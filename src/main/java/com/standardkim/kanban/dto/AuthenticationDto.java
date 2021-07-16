package com.standardkim.kanban.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthenticationDto {
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
	public static class LoginUserRequest {
		@NotBlank
		@Size(min = 5, max = 20)
		private String login;

		@NotBlank
		@Size(min = 5, max = 20)
		private String password;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class AuthenticationToken {
		String accessToken;
		String refreshToken;

		@Builder
		public AuthenticationToken(String accessToken, String refreshToken) {
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
		}
	}
}
