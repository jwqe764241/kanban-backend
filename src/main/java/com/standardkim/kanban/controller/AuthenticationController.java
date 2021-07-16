package com.standardkim.kanban.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.standardkim.kanban.dto.AuthenticationDto.AuthenticationToken;
import com.standardkim.kanban.dto.AuthenticationDto.LoginUserRequest;
import com.standardkim.kanban.service.AuthenticationService;
import com.standardkim.kanban.util.AuthenticationUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

	@Value("${authentication.refresh-token-ttl}")
	private int refreshTokenTTL;

	@PostMapping("/auth/login")
	@ResponseStatus(HttpStatus.OK)
	public String login(@RequestBody @Valid LoginUserRequest loginUserRequest, HttpServletResponse response) throws Exception {
		AuthenticationToken authenticationToken = authenticationService.getAuthenticationToken(
			loginUserRequest.getLogin(), 
			loginUserRequest.getPassword());
		
		//refresh token은 쿠키에 저장
		Cookie refreshTokenCookie = new Cookie("REFRESH_TOKEN", authenticationToken.getRefreshToken());
		refreshTokenCookie.setMaxAge(refreshTokenTTL);
		refreshTokenCookie.setHttpOnly(true);
		response.addCookie(refreshTokenCookie);

		//access token은 payload로 전송
		return authenticationToken.getAccessToken();
	}

	@PostMapping("/auth/refresh-access-token")
	@ResponseStatus(HttpStatus.OK)
	public String refreshAccessToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AuthenticationToken token = AuthenticationUtil.getAuthenticationTokens(request);
		String newAccessToken = authenticationService.refreshAccessToken(token.getAccessToken(), token.getRefreshToken());
		return newAccessToken;
	}

	@GetMapping("/welcome")
	@ResponseStatus(HttpStatus.OK)
	public String welcome() {
		return "Welcome";
	}
}
