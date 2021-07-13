package com.standardkim.kanban.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.standardkim.kanban.dto.AuthenticationDto.LoginUserRequest;
import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.service.AuthenticationService;
import com.standardkim.kanban.util.JwtTokenProvider;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
	private final AuthenticationService authenticationService;

	private final JwtTokenProvider jwtTokenProvider;

	private final PasswordEncoder passwordEncoder;

	@PostMapping("/auth/login")
	@ResponseStatus(HttpStatus.OK)
	public String login(@RequestBody @Valid LoginUserRequest loginUserRequest, HttpServletResponse response) throws Exception {
		SecurityUser securityUser = null;

		try {
			securityUser = (SecurityUser) authenticationService.loadUserByUsername(loginUserRequest.getLogin());
		}
		catch (UsernameNotFoundException e) {
			throw e;
		}

		if(!passwordEncoder.matches(loginUserRequest.getPassword(), securityUser.getPassword())) {
			throw new Exception("Password not matched");
		}

		String refreshToken = jwtTokenProvider.buildRefreshToken();
		String accessToken = jwtTokenProvider.buildAccessToken(securityUser.getLogin(), securityUser.getName());

		//DB에 refershToken 등록 이미 있으면 교체
		authenticationService.saveRefreshToken(securityUser.getId(), refreshToken);

		//refresh token은 쿠키에 저장
		Cookie refreshTokenCookie = new Cookie("REFRESH_TOKEN", refreshToken);
		refreshTokenCookie.setMaxAge(60);
		refreshTokenCookie.setHttpOnly(true);
		response.addCookie(refreshTokenCookie);

		//access token은 payload로 전송
		return accessToken;
	}

	@PostMapping("/auth/refresh-access-token")
	@ResponseStatus(HttpStatus.OK)
	public void refreshAccessToken(@RequestBody String accessToken, HttpServletResponse response) {
		
	}
}
