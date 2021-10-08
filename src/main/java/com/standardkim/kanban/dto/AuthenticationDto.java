package com.standardkim.kanban.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthenticationDto {
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Builder
	@AllArgsConstructor
	public static class SecurityUser implements UserDetails {
		private Long id;
		private String login;
		private String password;
		private String name;
		private LocalDateTime registerDate;
		private Collection<? extends GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

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
	public static class LoginParameter {
		@NotBlank
		@Size(min = 5, max = 20)
		private String login;

		@NotBlank
		@Size(min = 5, max = 20)
		private String password;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Builder
	@AllArgsConstructor
	public static class AuthenticationToken {
		private String accessToken;
		private String refreshToken;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Builder
	@AllArgsConstructor
	public static class AuthorizationHeader { 
		private String type;
		private String credentials;
	}
}
