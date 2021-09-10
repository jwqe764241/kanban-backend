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
import com.standardkim.kanban.repository.UserRepository;
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
	private final RefreshTokenRepository refreshTokenRepository;

	private final UserRepository userRepository;

	private final JwtTokenProvider jwtTokenProvider;

	private final PasswordEncoder passwordEncoder;

	private final ModelMapper modelMapper;

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByLogin(username)
			.orElseThrow(() -> new UsernameNotFoundException("cannot find user"));

		SecurityUser securityUser = modelMapper.map(user, SecurityUser.class);

		return securityUser;
	}

	public AuthenticationToken getAuthenticationToken(String login, String password) {
		SecurityUser securityUser = null;

		try {
			securityUser = (SecurityUser) loadUserByUsername(login);
		}
		catch (UsernameNotFoundException e) {
			throw new LoginFailedException("user not found");
		}

		if(!passwordEncoder.matches(password, securityUser.getPassword())) {
			throw new LoginFailedException("password not matched");
		}

		String refreshToken = jwtTokenProvider.buildRefreshToken(securityUser.getLogin(), securityUser.getName());
		String accessToken = jwtTokenProvider.buildAccessToken(securityUser.getLogin(), securityUser.getName());

		//DB에 refershToken 등록 이미 있으면 교체
		saveRefreshToken(securityUser.getId(), refreshToken);

		return AuthenticationToken.builder()
			.accessToken("Bearer " + accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveRefreshToken(Long userId, String token) {
		RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId).orElse(null);

		if(refreshToken == null) {
			User user = userRepository.findById(userId)
				.orElseThrow(() -> new UsernameNotFoundException("user not found"));

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

	@Transactional(rollbackFor = Exception.class)
	public void removeRefreshToken(String refreshToken) {
		String login = jwtTokenProvider.getLogin(refreshToken);
		Optional<User> user = userRepository.findByLogin(login);
		if(user.isPresent()) {
			refreshTokenRepository.deleteByUserId(user.get().getId());
		};
	}

	@Transactional(rollbackFor = Exception.class, noRollbackFor = ExpiredRefreshTokenException.class)
	public String refreshAccessToken(String refreshToken) throws Exception{
		if(refreshToken == null || refreshToken.isBlank()) 
			throw new TokenNotProvidedException("token must not be null");
		
		String login = jwtTokenProvider.getLogin(refreshToken);

		User user = userRepository.findByLogin(login)
			.orElseThrow(() -> new UserNotFoundException("user not found"));

		RefreshToken token = refreshTokenRepository.findByUserId(user.getId())
			.orElseThrow(() -> new RefreshTokenNotFoundException("refresh token not found"));

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
}
