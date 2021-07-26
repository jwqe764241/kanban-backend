package com.standardkim.kanban.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.standardkim.kanban.dto.AuthenticationDto.AuthenticationToken;

public class AuthenticationUtil {
	public static AuthenticationToken getAuthenticationTokens(HttpServletRequest request, String tokenName) {
		String accessToken = request.getHeader("Authorization");
		String refreshToken = null;

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(tokenName))
					refreshToken = cookie.getValue();
			}
		}

		return AuthenticationToken.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
