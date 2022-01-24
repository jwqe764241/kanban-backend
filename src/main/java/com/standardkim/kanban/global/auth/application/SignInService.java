package com.standardkim.kanban.global.auth.application;

import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenSaveService;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.domain.user.exception.UserNotFoundException;
import com.standardkim.kanban.global.auth.dto.AuthenticationToken;
import com.standardkim.kanban.global.auth.dto.LoginParam;
import com.standardkim.kanban.global.auth.exception.CannotLoginException;
import com.standardkim.kanban.global.util.JwtTokenProvider;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignInService {
	private final UserFindService userFindService;

	private final RefreshTokenSaveService refreshTokenSaveService;

	private final JwtTokenProvider jwtTokenProvider;

	private final PasswordEncoder passwordEncoder;

	@Transactional(rollbackFor = Exception.class)
	public AuthenticationToken signIn(LoginParam loginParam, Long refreshTokenTTL, Long accessTokenTTL) {
		User user = null;
		try {
			user = userFindService.findByUsername(loginParam.getUsername());
		}
		catch (UserNotFoundException e) {
			throw new CannotLoginException("incorrect username or password");
		}

		if(!passwordEncoder.matches(loginParam.getPassword(), user.getPassword())) {
			throw new CannotLoginException("incorrect username or password");
		}

		String refreshToken = jwtTokenProvider.build(user.getUsername(), user.getName(), refreshTokenTTL);
		String accessToken = jwtTokenProvider.build(user.getUsername(), user.getName(), accessTokenTTL);

		refreshTokenSaveService.save(user.getId(), refreshToken);

		return AuthenticationToken.of(accessToken, refreshToken);
	}
}
