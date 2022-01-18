package com.standardkim.kanban.service;

import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenDeleteService;
import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenFindService;
import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenSaveService;
import com.standardkim.kanban.domain.refreshtoken.domain.RefreshToken;
import com.standardkim.kanban.domain.refreshtoken.exception.RefreshTokenNotFoundException;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.domain.user.exception.UserNotFoundException;
import com.standardkim.kanban.global.auth.application.AuthenticationService;
import com.standardkim.kanban.global.auth.dto.AuthenticationToken;
import com.standardkim.kanban.global.auth.dto.LoginParam;
import com.standardkim.kanban.global.auth.exception.CannotLoginException;
import com.standardkim.kanban.global.auth.exception.ExpiredRefreshTokenException;
import com.standardkim.kanban.global.auth.exception.InvalidRefreshTokenException;
import com.standardkim.kanban.global.auth.exception.UnknownRefreshTokenException;
import com.standardkim.kanban.global.util.JwtTokenProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
	@Mock
	UserFindService userFindService;

	@Mock
	RefreshTokenFindService refreshTokenFindService;

	@Mock
	RefreshTokenSaveService refreshTokenSaveService;

	@Mock
	RefreshTokenDeleteService refreshTokenDeleteService;

	@Mock
	JwtTokenProvider jwtTokenProvider;

	@Spy
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@InjectMocks
	AuthenticationService authenticationService;

	@Test
	void login_UserIsExistAndPasswordMatched_AuthenticationToken() {
		given(userFindService.findByLogin("example")).willReturn(getUser("example"));
		given(jwtTokenProvider.build("example", "example", 20L)).willReturn("refresh-token");
		given(jwtTokenProvider.build("example", "example", 10L)).willReturn("access-token");
		given(refreshTokenSaveService.save(1L, "refresh-token")).willReturn(getRefreshToken("example"));

		AuthenticationToken token = authenticationService.login(getLoginParam(), 20L, 10L);

		assertThat(token).isNotNull();
		assertThat(token.getRefreshToken()).isEqualTo("refresh-token");
		assertThat(token.getAccessToken()).isEqualTo("access-token");
	}

	@Test
	void login_UserIsNotExist_ThrowCannotLoginException() {
		given(userFindService.findByLogin("example")).willThrow(new UserNotFoundException(""));

		assertThatThrownBy(() -> {
			authenticationService.login(getLoginParam(), 20L, 10L);
		}).isInstanceOf(CannotLoginException.class);
	}

	@Test
	void login_PasswordNotMatched_ThrowCannotLoginException() {
		given(userFindService.findByLogin("example")).willReturn(getUser("example1"));

		assertThatThrownBy(() -> {
			authenticationService.login(getLoginParam(), 20L, 10L);
		}).isInstanceOf(CannotLoginException.class);
	}

	@Test
	void logout_UserIsNotExist_DoesNotThrowAnyException() {
		given(jwtTokenProvider.getLogin(anyString())).willReturn("example");
		given(userFindService.findByLogin("example")).willThrow(new UserNotFoundException(""));

		assertThatCode(() -> {
			authenticationService.logout("refresh-token");
		}).doesNotThrowAnyException();
	}

	@Test
	void getAccessToken_UserIsNotExist_ThrowInvalidRefreshTokenException() {
		given(jwtTokenProvider.getLogin("refresh-token")).willReturn("example");
		given(userFindService.findByLogin("example")).willThrow(new UserNotFoundException(""));

		assertThatThrownBy(() -> {
			authenticationService.getAccessToken("refresh-token", 10L);
		}).isInstanceOf(InvalidRefreshTokenException.class);
	}

	@Test
	void getAccessToken_RefreshTokenIsNotExist_ThrowInvalidRefreshTokenException() {
		given(jwtTokenProvider.getLogin("refresh-token")).willReturn("example");
		given(userFindService.findByLogin("example")).willReturn(getUser(""));
		given(refreshTokenFindService.findById(1L)).willThrow(new RefreshTokenNotFoundException(""));

		assertThatThrownBy(() -> {
			authenticationService.getAccessToken("refresh-token", 10L);
		}).isInstanceOf(InvalidRefreshTokenException.class);
	}

	@Test
	void getAccessToken_RefreshTokenNotMatched_ThrowUnknownRefreshTokenException() {
		given(jwtTokenProvider.getLogin("refresh-token")).willReturn("example");
		given(userFindService.findByLogin("example")).willReturn(getUser(""));
		given(refreshTokenFindService.findById(1L)).willReturn(getRefreshToken("example1"));

		assertThatThrownBy(() -> {
			authenticationService.getAccessToken("refresh-token", 10L);
		}).isInstanceOf(UnknownRefreshTokenException.class);
	}

	@Test
	void getAccessToken_RefreshTokenIsExpired_ThrowExpiredRefreshTokenException() {
		given(jwtTokenProvider.getLogin("refresh-token")).willReturn("example");
		given(userFindService.findByLogin("example")).willReturn(getUser(""));
		given(refreshTokenFindService.findById(1L)).willReturn(getRefreshToken("refresh-token"));
		given(jwtTokenProvider.isExpired("refresh-token")).willReturn(true);

		assertThatThrownBy(() -> {
			authenticationService.getAccessToken("refresh-token", 10L);
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
}
