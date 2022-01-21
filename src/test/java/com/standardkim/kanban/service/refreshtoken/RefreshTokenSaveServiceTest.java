package com.standardkim.kanban.service.refreshtoken;

import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenFindService;
import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenSaveService;
import com.standardkim.kanban.domain.refreshtoken.dao.RefreshTokenRepository;
import com.standardkim.kanban.domain.refreshtoken.domain.RefreshToken;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.domain.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenSaveServiceTest {
	@Mock
	private RefreshTokenFindService refreshTokenFindService;

	@Mock
	private UserFindService userFindService;

	@Mock
	private RefreshTokenRepository refreshTokenRepository;

	@InjectMocks
	private RefreshTokenSaveService refreshTokenSaveService;

	@Test
	void save_RefreshTokenIsExist_Update() {
		given(refreshTokenFindService.isExist(1L)).willReturn(true);
		given(refreshTokenFindService.findById(1L)).willReturn(getRefreshToken(1L, "example"));

		RefreshToken refreshToken = refreshTokenSaveService.save(1L, "example");

		assertThat(refreshToken).isNotNull();
	}

	@Test
	void save_RefreshTokenIsNotExist_Create() {
		given(refreshTokenFindService.isExist(1L)).willReturn(false);
		given(userFindService.findById(1L)).willReturn(getUser(1L));
		given(refreshTokenRepository.save(any(RefreshToken.class))).willReturn(getRefreshToken(1L , "example"));
	
		RefreshToken refreshToken = refreshTokenSaveService.save(1L, "example");

		assertThat(refreshToken).isNotNull();
	}

	private RefreshToken getRefreshToken(Long userId, String token) {
		return RefreshToken.builder()
			.userId(userId)
			.token(token)
			.build();
	}

	private User getUser(Long id) {
		return User.builder()
			.id(id)
			.build();
	}
}
