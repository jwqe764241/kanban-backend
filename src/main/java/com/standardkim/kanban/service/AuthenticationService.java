package com.standardkim.kanban.service;

import javax.servlet.http.HttpServletRequest;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.entity.RefreshToken;
import com.standardkim.kanban.entity.User;
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
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {
	private final RefreshTokenRepository refreshTokenRepository;

	private final UserRepository userRepository;

	private final JwtTokenProvider jwtTokenProvider;

	@Override
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

	public String refreshAccessToken(String accessToken, String refreshToken) throws Exception {
		if(accessToken == null || refreshToken == null) 
			throw new NullPointerException("AccessToken or RefreshToken must not be null");
		
		String login = jwtTokenProvider.getLogin(accessToken);
		User user = userRepository.findByLogin(login);
		RefreshToken token = refreshTokenRepository.findByUserId(user.getId());
		
		if(!token.getToken().equals(refreshToken)) 
			throw new Exception("Refresh token does not matched");

		String newAccessToken = jwtTokenProvider.buildAccessToken(user.getLogin(), user.getName());
		return newAccessToken;
	}
}
