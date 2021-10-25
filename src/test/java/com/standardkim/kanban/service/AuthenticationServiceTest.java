package com.standardkim.kanban.service;

import java.util.Optional;

import com.standardkim.kanban.dto.AuthenticationDto.AuthenticationToken;
import com.standardkim.kanban.dto.AuthenticationDto.LoginParam;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.auth.CannotLoginException;
import com.standardkim.kanban.exception.auth.RefreshTokenNotFoundException;
import com.standardkim.kanban.exception.user.UserNotFoundException;
import com.standardkim.kanban.repository.RefreshTokenRepository;
import com.standardkim.kanban.util.JwtTokenProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
	@Mock
	UserService userService;

	@Mock
	RefreshTokenRepository refreshTokenRepository;

	@Spy
	JwtTokenProvider jwtTokenProvider;

	@Spy
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@InjectMocks
	AuthenticationService authenticationService;

	@Test
	void findRefreshTokenByUserId_RefreshTokenIsNotExist_ThrowRefreshTokenNotFoundException() {
		given(refreshTokenRepository.findByUserId(1L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			authenticationService.findRefreshTokenByUserId(1L);
		}).isInstanceOf(RefreshTokenNotFoundException.class);
	}

	@Test
	void issueAuthenticationToken_UserIsExistAndPasswordMatched_AuthenticationToken() {
		given(userService.findByLogin("example")).willReturn(getUser("example"));
		given(jwtTokenProvider.buildRefreshToken("example", "example")).willReturn("11111");
		given(jwtTokenProvider.buildAccessToken("example", "example")).willReturn("11111");

		AuthenticationToken token = authenticationService.issueAuthenticationToken(getLoginParam());

		assertThat(token).isNotNull();
	}

	@Test
	void issueAuthenticationToken_UserIsNotExist_ThrowCannotLoginException() {
		given(userService.findByLogin("example")).willThrow(new UserNotFoundException(""));

		assertThatThrownBy(() -> {
			authenticationService.issueAuthenticationToken(getLoginParam());
		}).isInstanceOf(CannotLoginException.class);
	}

	@Test
	void issueAuthenticationToken_PasswordNotMatched_ThrowCannotLoginException() {
		given(userService.findByLogin("example")).willReturn(getUser("example1"));

		assertThatThrownBy(() -> {
			authenticationService.issueAuthenticationToken(getLoginParam());
		}).isInstanceOf(CannotLoginException.class);
	}

	private LoginParam getLoginParam() {
		return LoginParam.builder()
			.login("example")
			.password("example")
			.build();
	}

	private User getUser(String rawPassword) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return User.builder()
			.id(1L)
			.login("example")
			.name("example")
			.password(encoder.encode(rawPassword))
			.build();
	}
}
