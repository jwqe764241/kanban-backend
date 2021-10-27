package com.standardkim.kanban.service;

import com.standardkim.kanban.entity.RefreshToken;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.auth.RefreshTokenNotFoundException;
import com.standardkim.kanban.repository.RefreshTokenRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {
	@Mock
	RefreshTokenRepository refreshTokenRepository;

	@Mock
	UserService userService;

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
		given(userService.findById(1L)).willReturn(getUser());
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
