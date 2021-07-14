package com.standardkim.kanban.util;

import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.standardkim.kanban.dto.AuthenticationDto.AuthenticationToken;

public class AuthenticationUtil {
	public static AuthenticationToken getAuthenticationTokens(HttpServletRequest request) {
		String accessToken = request.getHeader("Authorization");
		String refreshToken = null;

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("REFRESH_TOKEN"))
					refreshToken = cookie.getValue();
			}
		}

		return AuthenticationToken.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
