package com.standardkim.kanban.global.util;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class JwtTokenProvider {
	private final Key signKey;
	private final JwtParser jwtParser;

	public JwtTokenProvider(String secret) {
		this.signKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		this.jwtParser = Jwts.parserBuilder().setSigningKey(signKey).build();
	}

	public String build(String username, String name, Long ttl) {
		Claims claims = Jwts.claims();
		claims.put("username", username);
		claims.put("name", name);

		return build(claims, ttl);
	}

	public String build(Claims claims, Long ttl) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expiredAt = now.plus(ttl, ChronoUnit.SECONDS);
		
		return Jwts.builder()
			.setClaims(claims)
			.setExpiration(Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant()))
			.signWith(signKey)
			.compact();
	}

	public boolean isExpired(String token) {
		try {
			jwtParser.parseClaimsJws(token);
		} catch (ExpiredJwtException e) {
			return true;
		}

		return false;
	}

	public boolean isValid(String claimsJws) {
		try {
			jwtParser.parseClaimsJws(claimsJws);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	public String getUsername(String token) {
		String username = null;

		try {
			username = jwtParser.parseClaimsJws(token)
				.getBody()
				.get("username", String.class);
		} catch (ExpiredJwtException e) {
			username = e.getClaims().get("username", String.class);
		} catch (Exception e) {
			return null;
		}

		return username;
	}
}