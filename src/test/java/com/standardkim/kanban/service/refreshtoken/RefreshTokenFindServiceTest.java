package com.standardkim.kanban.service.refreshtoken;

import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenFindService;
import com.standardkim.kanban.domain.refreshtoken.dao.RefreshTokenRepository;
import com.standardkim.kanban.domain.refreshtoken.domain.RefreshToken;
import com.standardkim.kanban.domain.refreshtoken.exception.RefreshTokenNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenFindServiceTest {
	@Mock
	private RefreshTokenRepository refreshTokenRepository;

	@InjectMocks
	private RefreshTokenFindService refreshTokenFindService;

	@Test
	void isExist_RefreshTokenIsExist_True() {
		given(refreshTokenRepository.existsById(1L)).willReturn(true);

		boolean isExist = refreshTokenFindService.isExist(1L);

		assertThat(isExist).isTrue();
	}

	@Test
	void isExist_RefreshTokenIsNotExist_False() {
		given(refreshTokenRepository.existsById(1L)).willReturn(false);

		boolean isExist = refreshTokenFindService.isExist(1L);

		assertThat(isExist).isFalse();
	}

	@Test
	void findById_RefreshTokenIsExist_RefreshToken() {
		given(refreshTokenRepository.findById(1L)).willReturn(Optional.of(getRefreshToken(1L)));
	
		RefreshToken refreshToken = refreshTokenFindService.findById(1L);

		assertThat(refreshToken).isNotNull();
	}

	@Test
	void findById_RefreshTokenIsNotExist_ThrowRefreshTokenNotFoundException() {
		given(refreshTokenRepository.findById(1L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			refreshTokenFindService.findById(1L);
		}).isInstanceOf(RefreshTokenNotFoundException.class);
	}

	private RefreshToken getRefreshToken(Long userId) {
		return RefreshToken.builder()
			.userId(userId)
			.token("example")
			.build();
	}
}
