package com.standardkim.kanban.global.auth.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import com.standardkim.kanban.domain.user.domain.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class SecurityUser implements UserDetails {
	private Long id;
	private String login;
	private String password;
	private String name;
	private LocalDateTime createdAt;
	@Builder.Default
	private Collection<? extends GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

	public static SecurityUser of(User user ) {
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
