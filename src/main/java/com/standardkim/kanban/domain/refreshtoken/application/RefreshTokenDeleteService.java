package com.standardkim.kanban.domain.refreshtoken.application;

import com.standardkim.kanban.domain.refreshtoken.dao.RefreshTokenRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenDeleteService {
	private final RefreshTokenFindService refreshTokenFindService;

	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional(rollbackFor = Exception.class)
	public void delete(Long userId) {
		if(refreshTokenFindService.isExist(userId)) {
			refreshTokenRepository.deleteById(userId);
		}
	}
}
