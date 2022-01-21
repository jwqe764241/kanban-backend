package com.standardkim.kanban.service.auth;

import static org.mockito.ArgumentMatchers.anyString;

import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenDeleteService;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.exception.UserNotFoundException;
import com.standardkim.kanban.global.auth.application.SignOutService;
import com.standardkim.kanban.global.util.JwtTokenProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SignOutServiceTest {
	@Mock
	private UserFindService userFindService;

	@Mock
	private RefreshTokenDeleteService refreshTokenDeleteService;

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@InjectMocks
	private SignOutService signOutService;

	@Test
	void logout_UserIsNotExist_DoesNotThrowAnyException() {
		given(jwtTokenProvider.getLogin(anyString())).willReturn("example");
		given(userFindService.findByLogin("example")).willThrow(new UserNotFoundException(""));

		assertThatCode(() -> {
			signOutService.signOut("refresh-token");
		}).doesNotThrowAnyException();
	}
}
