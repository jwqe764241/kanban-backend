package com.standardkim.kanban.service.auth;

import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenSaveService;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.domain.user.exception.UserNotFoundException;
import com.standardkim.kanban.global.auth.application.SignInService;
import com.standardkim.kanban.global.auth.dto.AuthenticationToken;
import com.standardkim.kanban.global.auth.dto.SignInParam;
import com.standardkim.kanban.global.auth.exception.CannotSignInException;
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
		given(userFindService.findByUsername("example")).willReturn(getUser("example", "user", "pass"));
		given(jwtTokenProvider.build("example", "user", 20L)).willReturn("refresh-token");
		given(jwtTokenProvider.build("example", "user", 10L)).willReturn("access-token");
		given(refreshTokenSaveService.save(null, "refresh-token")).willReturn(null);

		AuthenticationToken token = signInService.signIn(getSignInParam("example", "pass"), 20L, 10L);

		assertThat(token).isNotNull();
		assertThat(token.getRefreshToken()).isEqualTo("refresh-token");
		assertThat(token.getAccessToken()).isEqualTo("access-token");
	}

	@Test
	void signIn_UserIsNotExist_ThrowCannotSignInException() {
		given(userFindService.findByUsername("example")).willThrow(new UserNotFoundException(""));

		assertThatThrownBy(() -> {
			signInService.signIn(getSignInParam("example", "example"), 20L, 10L);
		}).isInstanceOf(CannotSignInException.class);
	}

	@Test
	void signIn_PasswordNotMatched_ThrowCannotSignInException() {
		given(userFindService.findByUsername("example")).willReturn(getUser("example", "user", "wrongPass"));

		assertThatThrownBy(() -> {
			signInService.signIn(getSignInParam("example", "example"), 20L, 10L);
		}).isInstanceOf(CannotSignInException.class);
	}

	private SignInParam getSignInParam(String username, String password) {
		return SignInParam.builder()
			.username(username)
			.password(password)
			.build();
	}

	private User getUser(String username, String name, String rawPassword) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return User.builder()
			.username(username)
			.name(name)
			.password(encoder.encode(rawPassword))
			.build();
	}
}
