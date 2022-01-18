package com.standardkim.kanban.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenService;
import com.standardkim.kanban.domain.refreshtoken.dao.RefreshTokenRepository;
import com.standardkim.kanban.domain.refreshtoken.domain.RefreshToken;
import com.standardkim.kanban.domain.refreshtoken.exception.RefreshTokenNotFoundException;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.domain.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {
	@Mock
	RefreshTokenRepository refreshTokenRepository;

	@Mock
	UserFindService userFindService;

	@InjectMocks
	@Spy
	RefreshTokenService refreshTokenService;

	@Test
	void isExist_RefreshTokenIsExist_True() {
		given(refreshTokenRepository.existsById(1L)).willReturn(true);

		boolean isExist = refreshTokenService.isExist(1L);

		assertThat(isExist).isTrue();
	}

	@Test
	void isExist_RefreshTokenIsNotExist_False() {
		given(refreshTokenRepository.existsById(1L)).willReturn(false);

		boolean isExist = refreshTokenService.isExist(1L);

		assertThat(isExist).isFalse();
	}

	@Test
	void findById_RefreshTokenIsExist_RefreshToken() {
		given(refreshTokenRepository.findById(1L)).willReturn(Optional.of(getRefreshToken()));
	
		RefreshToken refreshToken = refreshTokenService.findById(1L);

		assertThat(refreshToken).isNotNull();
	}

	@Test
	void findById_RefreshTokenIsNotExist_ThrowRefreshTokenNotFoundException() {
		given(refreshTokenRepository.findById(1L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			refreshTokenService.findById(1L);
		}).isInstanceOf(RefreshTokenNotFoundException.class);
	}

	@Test
	void save_RefreshTokenIsExist_Update() {
		given(refreshTokenRepository.existsById(1L)).willReturn(true);
		given(refreshTokenRepository.findById(1L)).willReturn(Optional.of(getRefreshToken()));

		RefreshToken refreshToken = refreshTokenService.save(1L, "example");

		assertThat(refreshToken).isNotNull();
		verify(refreshTokenService).update(1L, "example");
	}

	@Test
	void save_RefreshTokenIsNotExist_Create() {
		given(refreshTokenRepository.existsById(1L)).willReturn(false);
		given(userFindService.findById(1L)).willReturn(getUser());
		given(refreshTokenRepository.save(any(RefreshToken.class))).willReturn(getRefreshToken());
	
		RefreshToken refreshToken = refreshTokenService.save(1L, "example");

		assertThat(refreshToken).isNotNull();
		verify(refreshTokenService).create(1L, "example");
	}

	@Test
	void delete_RefreshTokenIsExist_DoesNotThrowAnyException() {
		given(refreshTokenRepository.existsById(1L)).willReturn(true);

		assertThatCode(() -> {
			refreshTokenService.delete(1L);
		}).doesNotThrowAnyException();
	}

	private RefreshToken getRefreshToken() {
		return RefreshToken.builder()
			.userId(1L)
			.token("example")
			.build();
	}

	private User getUser() {
		return User.builder()
			.id(1L)
			.build();
	}
}
