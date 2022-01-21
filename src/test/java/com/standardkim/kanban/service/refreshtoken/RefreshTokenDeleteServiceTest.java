package com.standardkim.kanban.service.refreshtoken;

import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenDeleteService;
import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenFindService;
import com.standardkim.kanban.domain.refreshtoken.dao.RefreshTokenRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenDeleteServiceTest {
	@Mock
	private RefreshTokenFindService refreshTokenFindService;
	
	@Mock
	private RefreshTokenRepository refreshTokenRepository;

	@InjectMocks
	private RefreshTokenDeleteService refreshTokenDeleteService;

	@Test
	void delete_RefreshTokenIsExist_DoesCallDeleteById() {
		given(refreshTokenFindService.isExist(1L)).willReturn(true);

		refreshTokenDeleteService.delete(1L);

		verify(refreshTokenRepository).deleteById(1L);
	}

	@Test
	void delete_RefreshTokenIsNotExist_DoesNotCallDeleteById() {
		given(refreshTokenFindService.isExist(1L)).willReturn(false);

		refreshTokenDeleteService.delete(1L);

		verify(refreshTokenRepository, never()).deleteById(1L);
	}
}
