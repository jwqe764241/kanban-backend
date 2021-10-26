package com.standardkim.kanban.service;

import com.standardkim.kanban.dto.AuthenticationDto.AuthenticationToken;
import com.standardkim.kanban.dto.AuthenticationDto.LoginParam;
import com.standardkim.kanban.entity.RefreshToken;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.auth.CannotLoginException;
import com.standardkim.kanban.exception.auth.ExpiredRefreshTokenException;
import com.standardkim.kanban.exception.auth.InvalidRefreshTokenException;
import com.standardkim.kanban.exception.auth.RefreshTokenNotFoundException;
import com.standardkim.kanban.exception.auth.UnknownRefreshTokenException;
import com.standardkim.kanban.exception.user.UserNotFoundException;
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
	RefreshTokenService refreshTokenService;

	@Mock
	JwtTokenProvider jwtTokenProvider;

	@Spy
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@InjectMocks
	AuthenticationService authenticationService;

	@Test
	void login_UserIsExistAndPasswordMatched_AuthenticationToken() {
		given(userService.findByLogin("example")).willReturn(getUser("example"));
		given(jwtTokenProvider.buildAuthenticationToken("example", "example")).willReturn(getAuthenticationToken());
		given(refreshTokenService.save(1L, "example")).willReturn(getRefreshToken("example"));

		AuthenticationToken token = authenticationService.login(getLoginParam());

		assertThat(token).isNotNull();
	}

	@Test
	void login_UserIsNotExist_ThrowCannotLoginException() {
		given(userService.findByLogin("example")).willThrow(new UserNotFoundException(""));

		assertThatThrownBy(() -> {
			authenticationService.login(getLoginParam());
		}).isInstanceOf(CannotLoginException.class);
	}

	@Test
	void login_PasswordNotMatched_ThrowCannotLoginException() {
		given(userService.findByLogin("example")).willReturn(getUser("example1"));

		assertThatThrownBy(() -> {
			authenticationService.login(getLoginParam());
		}).isInstanceOf(CannotLoginException.class);
	}

	@Test
	void logout_UserIsNotExist_DoesNotThrowAnyException() {
		given(jwtTokenProvider.getLogin(anyString())).willReturn("example");
		given(userService.findByLogin("example")).willThrow(new UserNotFoundException(""));

		assertThatCode(() -> {
			authenticationService.logout("aaa");
		}).doesNotThrowAnyException();
	}

	@Test
	void getAccessToken_UserIsNotExist_ThrowInvalidRefreshTokenException() {
		given(jwtTokenProvider.getLogin("example")).willReturn("example");
		given(userService.findByLogin("example")).willThrow(new UserNotFoundException(""));

		assertThatThrownBy(() -> {
			authenticationService.getAccessToken("example");
		}).isInstanceOf(InvalidRefreshTokenException.class);
	}

	@Test
	void getAccessToken_RefreshTokenIsNotExist_ThrowInvalidRefreshTokenException() {
		given(jwtTokenProvider.getLogin("example")).willReturn("example");
		given(userService.findByLogin("example")).willReturn(getUser(""));
		given(refreshTokenService.findById(1L)).willThrow(new RefreshTokenNotFoundException(""));

		assertThatThrownBy(() -> {
			authenticationService.getAccessToken("example");
		}).isInstanceOf(InvalidRefreshTokenException.class);
	}

	@Test
	void getAccessToken_RefreshTokenNotMatched_ThrowUnknownRefreshTokenException() {
		given(jwtTokenProvider.getLogin("example")).willReturn("example");
		given(userService.findByLogin("example")).willReturn(getUser(""));
		given(refreshTokenService.findById(1L)).willReturn(getRefreshToken("example1"));

		assertThatThrownBy(() -> {
			authenticationService.getAccessToken("example");
		}).isInstanceOf(UnknownRefreshTokenException.class);
	}

	@Test
	void getAccessToken_RefreshTokenIsExpired_ThrowExpiredRefreshTokenException() {
		given(jwtTokenProvider.getLogin("example")).willReturn("example");
		given(userService.findByLogin("example")).willReturn(getUser(""));
		given(refreshTokenService.findById(1L)).willReturn(getRefreshToken("example"));
		given(jwtTokenProvider.isTokenExpired("example")).willReturn(true);

		assertThatThrownBy(() -> {
			authenticationService.getAccessToken("example");
		}).isInstanceOf(ExpiredRefreshTokenException.class);
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

	private RefreshToken getRefreshToken(String token) {
		return RefreshToken.builder()
			.userId(1L)
			.token(token)
			.build();
	}

	private AuthenticationToken getAuthenticationToken() {
		return AuthenticationToken.builder()
			.accessToken("example")
			.refreshToken("example")
			.build();
	}
}
