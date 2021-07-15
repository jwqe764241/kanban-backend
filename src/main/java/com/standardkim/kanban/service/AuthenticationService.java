package com.standardkim.kanban.service;

import javax.servlet.http.HttpServletRequest;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.entity.RefreshToken;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.ExpiredRefreshTokenException;
import com.standardkim.kanban.exception.RefreshTokenNotMatchedException;
import com.standardkim.kanban.exception.TokenNotProvidedException;
import com.standardkim.kanban.repository.RefreshTokenRepository;
import com.standardkim.kanban.repository.UserRepository;
import com.standardkim.kanban.util.JwtTokenProvider;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {
	private final RefreshTokenRepository refreshTokenRepository;

	private final UserRepository userRepository;

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByLogin(username);
		
		if (user == null)
			throw new UsernameNotFoundException("cannot find user");

		SecurityUser securityUser = SecurityUser.builder()
			.id(user.getId())
			.login(user.getLogin())
			.password(user.getPassword())
			.name(user.getName())
			.registerDate(user.getRegisterDate())
			.build();

		return securityUser;
	}

	@Transactional(rollbackFor = Exception.class)
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

	@Transactional(rollbackFor = Exception.class, noRollbackFor = ExpiredRefreshTokenException.class)
	public String refreshAccessToken(String accessToken, String refreshToken) {
		if((accessToken == null || accessToken.isBlank()) || (refreshToken == null || refreshToken.isBlank())) 
			throw new TokenNotProvidedException("token must not be null");
		
		String login = jwtTokenProvider.getLogin(accessToken);
		User user = userRepository.findByLogin(login);
		RefreshToken token = refreshTokenRepository.findByUserId(user.getId());
		String userRefreshToken = token.getToken();

		if(!userRefreshToken.equals(refreshToken)) {
			throw new RefreshTokenNotMatchedException("refresh token not matched");
		}

		if(jwtTokenProvider.isTokenExpired(userRefreshToken)) {
			refreshTokenRepository.delete(token);
			throw new ExpiredRefreshTokenException("refresh token is expired");
		}
 
		String newAccessToken = jwtTokenProvider.buildAccessToken(user.getLogin(), user.getName());
		return newAccessToken;
	}
}
