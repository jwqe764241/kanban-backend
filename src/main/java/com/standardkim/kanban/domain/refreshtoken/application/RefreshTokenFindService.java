package com.standardkim.kanban.domain.refreshtoken.application;

import com.standardkim.kanban.domain.refreshtoken.dao.RefreshTokenRepository;
import com.standardkim.kanban.domain.refreshtoken.domain.RefreshToken;
import com.standardkim.kanban.domain.refreshtoken.exception.RefreshTokenNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenFindService {
	private final RefreshTokenRepository refreshTokenRepository;
	
	@Transactional(readOnly = true)
	public boolean isExist(Long userId) {
		return refreshTokenRepository.existsById(userId);
	}

	@Transactional(readOnly = true)
	public RefreshToken findById(Long userId) {
		return refreshTokenRepository.findById(userId)
			.orElseThrow(() -> new RefreshTokenNotFoundException("refresh token not found"));
	}
}
