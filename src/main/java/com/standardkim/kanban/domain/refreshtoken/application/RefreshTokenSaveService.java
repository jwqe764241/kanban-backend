package com.standardkim.kanban.domain.refreshtoken.application;

import com.standardkim.kanban.domain.refreshtoken.dao.RefreshTokenRepository;
import com.standardkim.kanban.domain.refreshtoken.domain.RefreshToken;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.domain.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenSaveService {
	private final RefreshTokenFindService refreshTokenFindService;

	private final UserFindService userFindService;

	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional(rollbackFor = Exception.class)
	public RefreshToken create(Long userId, String token) {
		User user = userFindService.findById(userId);
		RefreshToken refreshToken = RefreshToken.of(user, token);
		return refreshTokenRepository.save(refreshToken);
	}

	@Transactional(rollbackFor = Exception.class)
	public RefreshToken update(Long userId, String token) {
		RefreshToken refreshToken = refreshTokenFindService.findById(userId);
		refreshToken.updateToken(token);
		return refreshToken;
	}

	@Transactional(rollbackFor = Exception.class)
	public RefreshToken save(Long userId, String token) {
		return refreshTokenFindService.isExist(userId) 
			? update(userId, token) : create(userId, token); 
	}
}
