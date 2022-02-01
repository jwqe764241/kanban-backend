package com.standardkim.kanban.global.auth.application;

import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenDeleteService;
import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenFindService;
import com.standardkim.kanban.domain.refreshtoken.domain.RefreshToken;
import com.standardkim.kanban.domain.refreshtoken.exception.RefreshTokenNotFoundException;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.domain.user.exception.UserNotFoundException;
import com.standardkim.kanban.global.auth.exception.ExpiredRefreshTokenException;
import com.standardkim.kanban.global.auth.exception.InvalidRefreshTokenException;
import com.standardkim.kanban.global.auth.exception.UnknownRefreshTokenException;
import com.standardkim.kanban.global.util.JwtTokenProvider;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccessTokenIssueService {
	private final UserFindService userFindService;
	
	private final RefreshTokenFindService refreshTokenFindService;
	
	private final RefreshTokenDeleteService refreshTokenDeleteService;

	private final JwtTokenProvider jwtTokenProvider;

	@Transactional(rollbackFor = Exception.class, noRollbackFor = ExpiredRefreshTokenException.class)
	public String issue(String token, Long ttl) {
		User user = null;
		RefreshToken refreshToken = null;
		try {
			user = userFindService.findByUsername(jwtTokenProvider.getUsername(token));
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
 
		return jwtTokenProvider.build(user.getUsername(), user.getName(), ttl);
	}
}
