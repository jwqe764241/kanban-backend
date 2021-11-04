package com.standardkim.kanban.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.standardkim.kanban.dto.AuthenticationDto.AccessToken;
import com.standardkim.kanban.dto.AuthenticationDto.AuthenticationToken;
import com.standardkim.kanban.dto.AuthenticationDto.LoginParam;
import com.standardkim.kanban.exception.auth.EmptyRefreshTokenException;
import com.standardkim.kanban.service.AuthenticationService;
import com.standardkim.kanban.util.CookieUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
	private final AuthenticationService authenticationService;

	@Value("${config.refresh-token-name}")
	private String refreshTokenName;

	@Value("${config.authentication.refresh-token-ttl}")
	private Long refreshTokenTTL;

	@Value("${config.authentication.access-token-ttl}")
	private Long accessTokenTTL;

	@Value("${config.authentication.ws-token-ttl}")
	private Long wsTokenTTL;

	@PostMapping("/auth/login")
	@ResponseStatus(HttpStatus.OK)
	public AccessToken login(@RequestBody @Valid LoginParam loginParam, HttpServletResponse response) throws Exception {
		//TODO:Add prev refresh token to blacklist
		AuthenticationToken authenticationToken = authenticationService.login(loginParam, refreshTokenTTL, accessTokenTTL);
		
		ResponseCookie cookie = ResponseCookie.from(refreshTokenName, authenticationToken.getRefreshToken())
			.domain("localhost")
			.path("/")
			.sameSite("Lax")
			.maxAge(refreshTokenTTL)
			.httpOnly(true)
			.build();
		response.setHeader("Set-Cookie", cookie.toString());

		return AccessToken.from(authenticationToken.getAccessToken());
	}

	@PostMapping("/auth/logout")
	@ResponseStatus(HttpStatus.OK)
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = CookieUtil.getValueFromHttpServletRequest(request, refreshTokenName);
		if(refreshToken == null)
			return;

		authenticationService.logout(refreshToken);

		ResponseCookie cookie = ResponseCookie.from(refreshTokenName, null)
			.domain("localhost")
			.path("/")
			.sameSite("Lax")
			.maxAge(0)
			.httpOnly(true)
			.build();
		response.setHeader("Set-Cookie", cookie.toString());
	}

	@GetMapping("/auth/access-token")
	@ResponseStatus(HttpStatus.OK)
	public AccessToken getAccessToken(HttpServletRequest request) throws Exception {
		String refreshToken = CookieUtil.getValueFromHttpServletRequest(request, refreshTokenName);
		if(refreshToken == null || refreshToken.isBlank()) {
			throw new EmptyRefreshTokenException("refresh token was empty");
		}
		String accessToken = authenticationService.getAccessToken(refreshToken, accessTokenTTL);
		return AccessToken.from(accessToken);
	}

	@GetMapping("/auth/ws-token")
	@ResponseStatus(HttpStatus.OK)
	public AccessToken getWebsocketToken(HttpServletRequest request) throws Exception {
		String refreshToken = CookieUtil.getValueFromHttpServletRequest(request, refreshTokenName);
		if(refreshToken == null || refreshToken.isBlank()) {
			throw new EmptyRefreshTokenException("refresh token was empty");
		}
		String accessToken = authenticationService.getAccessToken(refreshToken, wsTokenTTL);
		return AccessToken.from(accessToken);
	}

	@GetMapping("/auth/check-token")
	@ResponseStatus(HttpStatus.OK)
	public void checkToken() {}
}
