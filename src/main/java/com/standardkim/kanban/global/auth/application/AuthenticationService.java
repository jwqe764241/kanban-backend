package com.standardkim.kanban.global.auth.application;

import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenDeleteService;
import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenFindService;
import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenSaveService;
import com.standardkim.kanban.domain.refreshtoken.domain.RefreshToken;
import com.standardkim.kanban.domain.refreshtoken.exception.RefreshTokenNotFoundException;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.domain.user.exception.UserNotFoundException;
import com.standardkim.kanban.global.auth.dto.AuthenticationToken;
import com.standardkim.kanban.global.auth.dto.LoginParam;
import com.standardkim.kanban.global.auth.exception.CannotLoginException;
import com.standardkim.kanban.global.auth.exception.ExpiredRefreshTokenException;
import com.standardkim.kanban.global.auth.exception.InvalidRefreshTokenException;
import com.standardkim.kanban.global.auth.exception.UnknownRefreshTokenException;
import com.standardkim.kanban.global.util.JwtTokenProvider;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UserFindService userFindService;
	
	private final RefreshTokenFindService refreshTokenFindService;

	private final RefreshTokenSaveService refreshTokenSaveService;

	private final RefreshTokenDeleteService refreshTokenDeleteService;

	private final JwtTokenProvider jwtTokenProvider;

	private final PasswordEncoder passwordEncoder;

	@Transactional(rollbackFor = Exception.class)
	public AuthenticationToken login(LoginParam loginParam, Long refreshTokenTTL, Long accessTokenTTL) {
		User user = null;
		try {
			user = userFindService.findByLogin(loginParam.getLogin());
		}
		catch (UserNotFoundException e) {
			throw new CannotLoginException("incorrect username or password");
		}

		if(!passwordEncoder.matches(loginParam.getPassword(), user.getPassword())) {
			throw new CannotLoginException("incorrect username or password");
		}

		String refreshToken = jwtTokenProvider.build(user.getLogin(), user.getName(), refreshTokenTTL);
		String accessToken = jwtTokenProvider.build(user.getLogin(), user.getName(), accessTokenTTL);

		refreshTokenSaveService.save(user.getId(), refreshToken);

		return AuthenticationToken.of(accessToken, refreshToken);
	}

	@Transactional(rollbackFor = Exception.class)
	public void logout(String refreshToken) {
		String login = jwtTokenProvider.getLogin(refreshToken);
		try {
			User user = userFindService.findByLogin(login);
			refreshTokenDeleteService.delete(user.getId());
		} catch (UserNotFoundException e) {
			return;
		}
	}

	@Transactional(rollbackFor = Exception.class, noRollbackFor = ExpiredRefreshTokenException.class)
	public String getAccessToken(String token, Long ttl) {
		User user = null;
		RefreshToken refreshToken = null;
		try {
			user = userFindService.findByLogin(jwtTokenProvider.getLogin(token));
			refreshToken = refreshTokenFindService.findById(user.getId());
		} catch (UserNotFoundException | RefreshTokenNotFoundException e) {
			throw new InvalidRefreshTokenException("refresh token was invalid", e);
		}

		if(!refreshToken.isTokenMatched(token)) {
			throw new UnknownRefreshTokenException("unknown refresh token");
		}

		if(jwtTokenProvider.isExpired(refreshToken.getToken())) {
			refreshTokenDeleteService.delete(refreshToken.getUserId());
			throw new ExpiredRefreshTokenException("refresh token was expired");
		}
 
		return jwtTokenProvider.build(user.getLogin(), user.getName(), ttl);
	}
}
