package com.standardkim.kanban.service;

import java.util.Optional;

import com.standardkim.kanban.dto.AuthenticationDto.AuthenticationToken;
import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.entity.RefreshToken;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.ExpiredRefreshTokenException;
import com.standardkim.kanban.exception.LoginFailedException;
import com.standardkim.kanban.exception.RefreshTokenNotFoundException;
import com.standardkim.kanban.exception.RefreshTokenNotMatchedException;
import com.standardkim.kanban.exception.TokenNotProvidedException;
import com.standardkim.kanban.exception.UserNotFoundException;
import com.standardkim.kanban.repository.RefreshTokenRepository;
import com.standardkim.kanban.util.JwtTokenProvider;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {
	private final UserService userService;
	
	private final RefreshTokenRepository refreshTokenRepository;

	private final JwtTokenProvider jwtTokenProvider;

	private final PasswordEncoder passwordEncoder;

	private final ModelMapper modelMapper;

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			User user = userService.getUserByLogin(username);
			SecurityUser securityUser = modelMapper.map(user, SecurityUser.class);
			return securityUser;
		} catch (UserNotFoundException e) {
			throw new UsernameNotFoundException("cannot find user");
		}
	}

	@Transactional(readOnly = true)
	public RefreshToken getRefreshTokenByUserId(Long userId) {
		Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(userId);
		return refreshToken.orElseThrow(() -> new RefreshTokenNotFoundException("refresh token not found"));
	}

	@Transactional(rollbackFor = Exception.class)
	public AuthenticationToken getAuthenticationToken(String login, String password) {
		User user = null;

		try {
			user = userService.getUserByLogin(login);
		}
		catch (UserNotFoundException e) {
			throw new LoginFailedException("user not found");
		}

		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new LoginFailedException("password not matched");
		}

		String refreshToken = jwtTokenProvider.buildRefreshToken(user.getLogin(), user.getName());
		String accessToken = jwtTokenProvider.buildAccessToken(user.getLogin(), user.getName());

		//DB에 refershToken 등록 이미 있으면 교체
		saveRefreshToken(user.getId(), refreshToken);

		return AuthenticationToken.builder()
			.accessToken("Bearer " + accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	@Transactional(rollbackFor = Exception.class, noRollbackFor = ExpiredRefreshTokenException.class)
	public String refreshAccessToken(String refreshToken) throws Exception{
		if(refreshToken == null || refreshToken.isBlank()) {
			throw new TokenNotProvidedException("token must not be null");
		}
		
		String login = jwtTokenProvider.getLogin(refreshToken);
		User user = userService.getUserByLogin(login);
		RefreshToken token = getRefreshTokenByUserId(user.getId());
		String userRefreshToken = token.getToken();

		if(!userRefreshToken.equals(refreshToken)) {
			throw new RefreshTokenNotMatchedException("refresh token not matched");
		}

		if(jwtTokenProvider.isTokenExpired(userRefreshToken)) {
			refreshTokenRepository.delete(token);
			throw new ExpiredRefreshTokenException("refresh token is expired");
		}
 
		String newAccessToken = jwtTokenProvider.buildAccessToken(user.getLogin(), user.getName());
		return "Bearer " + newAccessToken;
	}

	@Transactional(rollbackFor = Exception.class)
	public RefreshToken createRefreshToken(Long userId, String token) {
		User user = userService.getUserById(userId);
		RefreshToken refreshToken = RefreshToken.builder()
			.user(user)
			.token(token)
			.build();
		refreshToken = refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}

	@Transactional(rollbackFor = Exception.class)
	public RefreshToken updatRefreshToken(RefreshToken refreshToken, String token) {
		refreshToken = refreshToken.toBuilder()
			.token(token)
			.build();
		refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveRefreshToken(Long userId, String token) {
		try{
			RefreshToken refreshToken = getRefreshTokenByUserId(userId);
			updatRefreshToken(refreshToken, token);
		} catch (RefreshTokenNotFoundException e) {
			createRefreshToken(userId, token);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void removeRefreshToken(String refreshToken) {
		String login = jwtTokenProvider.getLogin(refreshToken);
		try {
			User user = userService.getUserByLogin(login);
			refreshTokenRepository.deleteByUserId(user.getId());
		} catch (UserNotFoundException e) {
			return;
		}
	}
}
