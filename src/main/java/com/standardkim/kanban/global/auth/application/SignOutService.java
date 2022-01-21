package com.standardkim.kanban.global.auth.application;

import com.standardkim.kanban.domain.refreshtoken.application.RefreshTokenDeleteService;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.domain.user.exception.UserNotFoundException;
import com.standardkim.kanban.global.util.JwtTokenProvider;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignOutService {
	private final UserFindService userFindService;

	private final RefreshTokenDeleteService refreshTokenDeleteService;

	private final JwtTokenProvider jwtTokenProvider;

	@Transactional(rollbackFor = Exception.class)
	public void signOut(String refreshToken) {
		String login = jwtTokenProvider.getLogin(refreshToken);
		try {
			User user = userFindService.findByLogin(login);
			refreshTokenDeleteService.delete(user.getId());
		} catch (UserNotFoundException e) {
			return;
		}
	}
}
