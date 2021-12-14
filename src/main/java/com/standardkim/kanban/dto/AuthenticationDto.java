package com.standardkim.kanban.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.standardkim.kanban.entity.User;

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
		private LocalDateTime createdAt;
		private Collection<? extends GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		public static SecurityUser from(User user ) {
			return SecurityUser.builder()
				.id(user.getId())
				.login(user.getLogin())
				.password(user.getPassword())
				.name(user.getName())
				.createdAt(user.getCreatedAt())
				.build();
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
	@Builder
	@AllArgsConstructor
	public static class LoginParam {
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

		public static AuthenticationToken from(String accessToken, String refreshToken) {
			return AuthenticationToken.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Builder
	@AllArgsConstructor
	public static class AuthorizationHeader { 
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

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Builder
	@AllArgsConstructor
	public static class AccessToken {
		private String token;

		public static AccessToken from(String token) {
			return AccessToken.builder()
				.token(token)
				.build();
		}
	}
}
