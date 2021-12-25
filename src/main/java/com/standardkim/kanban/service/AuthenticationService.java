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

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UserService userService;
	
	private final RefreshTokenService refreshTokenService;

	private final JwtTokenProvider jwtTokenProvider;

	private final PasswordEncoder passwordEncoder;

	@Transactional(rollbackFor = Exception.class)
	public AuthenticationToken login(LoginParam loginParam, Long refreshTokenTTL, Long accessTokenTTL) {
		User user = null;
		try {
			user = userService.findByLogin(loginParam.getLogin());
		}
		catch (UserNotFoundException e) {
			throw new CannotLoginException("incorrect username or password");
		}

		if(!passwordEncoder.matches(loginParam.getPassword(), user.getPassword())) {
			throw new CannotLoginException("incorrect username or password");
		}

		String refreshToken = jwtTokenProvider.build(user.getLogin(), user.getName(), refreshTokenTTL);
		String accessToken = jwtTokenProvider.build(user.getLogin(), user.getName(), accessTokenTTL);

		refreshTokenService.save(user.getId(), refreshToken);

		return AuthenticationToken.from(accessToken, refreshToken);
	}

	@Transactional(rollbackFor = Exception.class)
	public void logout(String refreshToken) {
		String login = jwtTokenProvider.getLogin(refreshToken);
		try {
			User user = userService.findByLogin(login);
			refreshTokenService.delete(user.getId());
		} catch (UserNotFoundException e) {
			return;
		}
	}

	@Transactional(rollbackFor = Exception.class, noRollbackFor = ExpiredRefreshTokenException.class)
	public String getAccessToken(String token, Long ttl) {
		User user = null;
		RefreshToken refreshToken = null;
		try {
			user = userService.findByLogin(jwtTokenProvider.getLogin(token));
			refreshToken = refreshTokenService.findById(user.getId());
		} catch (UserNotFoundException | RefreshTokenNotFoundException e) {
			throw new InvalidRefreshTokenException("refresh token was invalid", e);
		}

		if(!refreshToken.isTokenMatched(token)) {
			throw new UnknownRefreshTokenException("unknown refresh token");
		}

		if(jwtTokenProvider.isExpired(refreshToken.getToken())) {
			refreshTokenService.delete(refreshToken.getUserId());
			throw new ExpiredRefreshTokenException("refresh token was expired");
		}
 
		return jwtTokenProvider.build(user.getLogin(), user.getName(), ttl);
	}
}
