package com.standardkim.kanban.service.auth;

import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenDeleteService;
import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenFindService;
import com.standardkim.kanban.domain.refreshtoken.domain.RefreshToken;
import com.standardkim.kanban.domain.refreshtoken.exception.RefreshTokenNotFoundException;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.domain.user.exception.UserNotFoundException;
import com.standardkim.kanban.global.auth.application.AccessTokenIssueService;
import com.standardkim.kanban.global.auth.exception.ExpiredRefreshTokenException;
import com.standardkim.kanban.global.auth.exception.InvalidRefreshTokenException;
import com.standardkim.kanban.global.auth.exception.UnknownRefreshTokenException;
import com.standardkim.kanban.global.util.JwtTokenProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AccessTokenIssueServiceTest {
	@Mock
	private UserFindService userFindService;

	@Mock
	private RefreshTokenFindService refreshTokenFindService;

	@Mock
	private RefreshTokenDeleteService refreshTokenDeleteService;

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@InjectMocks
	private AccessTokenIssueService accessTokenIssueService;

	@Test
	void issue_UserIsNotExist_ThrowInvalidRefreshTokenException() {
		given(jwtTokenProvider.getLogin("refresh-token")).willReturn("example");
		given(userFindService.findByLogin("example")).willThrow(new UserNotFoundException(""));

		assertThatThrownBy(() -> {
			accessTokenIssueService.issue("refresh-token", 10L);
		}).isInstanceOf(InvalidRefreshTokenException.class);
	}

	@Test
	void issue_RefreshTokenIsNotExist_ThrowInvalidRefreshTokenException() {
		given(jwtTokenProvider.getLogin("refresh-token")).willReturn("example");
		given(userFindService.findByLogin("example")).willReturn(getUser(""));
		given(refreshTokenFindService.findById(1L)).willThrow(new RefreshTokenNotFoundException(""));

		assertThatThrownBy(() -> {
			accessTokenIssueService.issue("refresh-token", 10L);
		}).isInstanceOf(InvalidRefreshTokenException.class);
	}

	@Test
	void issue_RefreshTokenNotMatched_ThrowUnknownRefreshTokenException() {
		given(jwtTokenProvider.getLogin("refresh-token")).willReturn("example");
		given(userFindService.findByLogin("example")).willReturn(getUser(""));
		given(refreshTokenFindService.findById(1L)).willReturn(getRefreshToken("example1"));

		assertThatThrownBy(() -> {
			accessTokenIssueService.issue("refresh-token", 10L);
		}).isInstanceOf(UnknownRefreshTokenException.class);
	}

	@Test
	void issue_RefreshTokenIsExpired_ThrowExpiredRefreshTokenException() {
		given(jwtTokenProvider.getLogin("refresh-token")).willReturn("example");
		given(userFindService.findByLogin("example")).willReturn(getUser(""));
		given(refreshTokenFindService.findById(1L)).willReturn(getRefreshToken("refresh-token"));
		given(jwtTokenProvider.isExpired("refresh-token")).willReturn(true);

		assertThatThrownBy(() -> {
			accessTokenIssueService.issue("refresh-token", 10L);
		}).isInstanceOf(ExpiredRefreshTokenException.class);
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
