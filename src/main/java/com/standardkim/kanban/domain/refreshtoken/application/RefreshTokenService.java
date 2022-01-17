package com.standardkim.kanban.domain.refreshtoken.application;

import com.standardkim.kanban.domain.refreshtoken.dao.RefreshTokenRepository;
import com.standardkim.kanban.domain.refreshtoken.domain.RefreshToken;
import com.standardkim.kanban.domain.refreshtoken.exception.RefreshTokenNotFoundException;
import com.standardkim.kanban.domain.user.application.UserService;
import com.standardkim.kanban.domain.user.domain.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

	private final UserService userService;

	@Transactional(readOnly = true)
	public boolean isExist(Long userId) {
		return refreshTokenRepository.existsById(userId);
	}

	@Transactional(readOnly = true)
	public RefreshToken findById(Long userId) {
		return refreshTokenRepository.findById(userId)
			.orElseThrow(() -> new RefreshTokenNotFoundException("refresh token not found"));
	}

	@Transactional(rollbackFor = Exception.class)
	public RefreshToken create(Long userId, String token) {
		User user = userService.findById(userId);
		RefreshToken refreshToken = RefreshToken.of(user, token);
		return refreshTokenRepository.save(refreshToken);
	}

	@Transactional(rollbackFor = Exception.class)
	public RefreshToken update(Long userId, String token) {
		RefreshToken refreshToken = findById(userId);
		refreshToken.updateToken(token);
		return refreshToken;
	}

	@Transactional(rollbackFor = Exception.class)
	public RefreshToken save(Long userId, String token) {
		return isExist(userId) ? update(userId, token) : create(userId, token); 
	}

	@Transactional(rollbackFor = Exception.class)
	public void delete(Long userId) {
		if(isExist(userId)) {
			refreshTokenRepository.deleteById(userId);
		}
	}
}