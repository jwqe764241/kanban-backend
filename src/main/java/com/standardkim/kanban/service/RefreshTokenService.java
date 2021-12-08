package com.standardkim.kanban.service;

import com.standardkim.kanban.entity.RefreshToken;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.auth.RefreshTokenNotFoundException;
import com.standardkim.kanban.repository.RefreshTokenRepository;

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
		RefreshToken refreshToken = RefreshToken.from(user, token);
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
