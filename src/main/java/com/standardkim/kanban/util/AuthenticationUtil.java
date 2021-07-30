package com.standardkim.kanban.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class AuthenticationUtil {
	public static String getRefreshToken(HttpServletRequest request, String tokenName) {
		String refreshToken = null;

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(tokenName))
					refreshToken = cookie.getValue();
			}
		}

		return refreshToken;
	}
}
