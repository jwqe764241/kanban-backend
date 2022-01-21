package com.standardkim.kanban.service.auth;

import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenSaveService;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.domain.user.exception.UserNotFoundException;
import com.standardkim.kanban.global.auth.application.SignInService;
import com.standardkim.kanban.global.auth.dto.AuthenticationToken;
import com.standardkim.kanban.global.auth.dto.LoginParam;
import com.standardkim.kanban.global.auth.exception.CannotLoginException;
import com.standardkim.kanban.global.util.JwtTokenProvider;

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
public class SignInServiceTest {
	@Mock
	private UserFindService userFindService;

	@Mock
	private RefreshTokenSaveService refreshTokenSaveService;

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@Spy
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@InjectMocks
	private SignInService signInService;

	@Test
	void signIn_UserIsExistAndPasswordMatched_AuthenticationToken() {
		given(userFindService.findByLogin("example")).willReturn(getUser("example", "user", "pass"));
		given(jwtTokenProvider.build("example", "user", 20L)).willReturn("refresh-token");
		given(jwtTokenProvider.build("example", "user", 10L)).willReturn("access-token");
		given(refreshTokenSaveService.save(null, "refresh-token")).willReturn(null);

		AuthenticationToken token = signInService.signIn(getLoginParam("example", "pass"), 20L, 10L);

		assertThat(token).isNotNull();
		assertThat(token.getRefreshToken()).isEqualTo("refresh-token");
		assertThat(token.getAccessToken()).isEqualTo("access-token");
	}

	@Test
	void signIn_UserIsNotExist_ThrowCannotLoginException() {
		given(userFindService.findByLogin("example")).willThrow(new UserNotFoundException(""));

		assertThatThrownBy(() -> {
			signInService.signIn(getLoginParam("example", "example"), 20L, 10L);
		}).isInstanceOf(CannotLoginException.class);
	}

	@Test
	void signIn_PasswordNotMatched_ThrowCannotLoginException() {
		given(userFindService.findByLogin("example")).willReturn(getUser("example", "user", "wrongPass"));

		assertThatThrownBy(() -> {
			signInService.signIn(getLoginParam("example", "example"), 20L, 10L);
		}).isInstanceOf(CannotLoginException.class);
	}

	private LoginParam getLoginParam(String login, String password) {
		return LoginParam.builder()
			.login(login)
			.password(password)
			.build();
	}

	private User getUser(String login, String name, String rawPassword) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return User.builder()
			.login(login)
			.name(name)
			.password(encoder.encode(rawPassword))
			.build();
	}
}
