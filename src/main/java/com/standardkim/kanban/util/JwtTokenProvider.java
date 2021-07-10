package com.standardkim.kanban.util;

import java.security.Key;
import java.util.Base64;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("{server.authentication.secret-key}")
	private String secret;

	private JwtParser jwtParser;

	@PostConstruct
	public void init() {
		secret = Base64.getEncoder().encodeToString(secret.getBytes());
		jwtParser = Jwts.parserBuilder().setSigningKey(secret).build();
	}

	public String buildToken() {

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