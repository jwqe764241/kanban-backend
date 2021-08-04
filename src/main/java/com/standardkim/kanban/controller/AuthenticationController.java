package com.standardkim.kanban.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.standardkim.kanban.dto.AuthenticationDto.AuthenticationToken;
import com.standardkim.kanban.dto.AuthenticationDto.LoginUserRequest;
import com.standardkim.kanban.service.AuthenticationService;
import com.standardkim.kanban.util.AuthenticationUtil;

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

	@Value("${config.authentication.refresh-token-ttl}")
	private int refreshTokenTTL;

	@Value("${config.refresh-token-name}")
	private String refreshTokenName;

	@PostMapping("/auth/login")
	@ResponseStatus(HttpStatus.OK)
	public String login(@RequestBody @Valid LoginUserRequest loginUserRequest, HttpServletResponse response) throws Exception {
		//TODO:Add prev refresh token to blacklist
		AuthenticationToken authenticationToken = authenticationService.getAuthenticationToken(
			loginUserRequest.getLogin(), 
			loginUserRequest.getPassword());
		
		ResponseCookie cookie = ResponseCookie.from(refreshTokenName, authenticationToken.getRefreshToken())
			.domain("localhost")
			.path("/")
			.sameSite("Lax")
			.maxAge(refreshTokenTTL)
			.httpOnly(true)
			.build();
		response.setHeader("Set-Cookie", cookie.toString());

		//access token은 payload로 전송
		return authenticationToken.getAccessToken();
	}

	@PostMapping("/auth/logout")
	@ResponseStatus(HttpStatus.OK)
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = AuthenticationUtil.getRefreshToken(request, refreshTokenName);
		authenticationService.removeRefreshToken(refreshToken);

		ResponseCookie cookie = ResponseCookie.from(refreshTokenName, null)
			.domain("localhost")
			.path("/")
			.sameSite("Lax")
			.maxAge(0)
			.httpOnly(true)
			.build();
		response.setHeader("Set-Cookie", cookie.toString());
	}

	@PostMapping("/auth/refresh-access-token")
	@ResponseStatus(HttpStatus.OK)
	public String refreshAccessToken(HttpServletRequest request) throws Exception {
		String refreshToken = AuthenticationUtil.getRefreshToken(request, refreshTokenName);
		String newAccessToken = authenticationService.refreshAccessToken(refreshToken);
		return newAccessToken;
	}

	@GetMapping("/auth/check-token")
	@ResponseStatus(HttpStatus.OK)
	public void checkToken() {}
}
