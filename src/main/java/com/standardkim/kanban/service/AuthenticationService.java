package com.standardkim.kanban.service;

import com.standardkim.kanban.entity.RefreshToken;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.repository.RefreshTokenRepository;
import com.standardkim.kanban.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AuthenticationService {
	private final RefreshTokenRepository refreshTokenRepository;

	private final UserRepository userRepository;

	public void saveRefreshToken(Long userId, String token) {
		RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId);

		if(refreshToken == null) {
			User user = userRepository.findById(userId).get();
			refreshToken = RefreshToken.builder()
				.user(user)
				.token(token)
				.build();
		}
		else {
			refreshToken = refreshToken.toBuilder()
				.token(token)
				.build();
		}
		
		refreshTokenRepository.save(refreshToken);
	}
}
