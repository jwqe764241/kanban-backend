package com.standardkim.kanban.util;

import java.util.Base64;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

@Component
public class JwtTokenProvider {

	@Value("{authentication.secret-key}")
	private String secret;

	private JwtParser jwtParser;

	@PostConstruct
	public void init() {
		secret = Base64.getEncoder().encodeToString(secret.getBytes());
		jwtParser = Jwts.parserBuilder().setSigningKey(secret).build();
	}

	public String buildToken() {
		return "";
	}

	public boolean validateToken(String claimsJws) {
		try {
			jwtParser.parseClaimsJws(claimsJws);
			return true;
		} catch (JwtException e) {
		}

		return false;
	}
}