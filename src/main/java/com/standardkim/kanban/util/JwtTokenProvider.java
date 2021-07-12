package com.standardkim.kanban.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${authentication.secret-key}")
	private String secret;

	@Value("${authentication.access-token-ttl}")
	private Long accessTokenTTL;

	@Value("${authentication.refresh-token-ttl}")
	private Long refreshTokenTTL;

	private Key signKey;

	private JwtParser jwtParser;

	@PostConstruct
	public void init() {
		signKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		jwtParser = Jwts.parserBuilder().setSigningKey(secret).build();
	}

	public String buildAccessToken(String login, String name) {
		Claims claims = Jwts.claims();
		claims.put("login", login);
		claims.put("name", name);

		return buildToken(claims, accessTokenTTL);
	}

	public String buildRefreshToken() {
		return buildToken(null, refreshTokenTTL);
	}

	public String buildToken(Claims claims, Long ttl) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expiredAt = now.plus(ttl, ChronoUnit.SECONDS);
		
		return Jwts.builder()
			.setClaims(claims)
			.setExpiration(Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant()))
			.signWith(signKey)
			.compact();
	}

	public boolean validateToken(String claimsJws, Integer ttl) {
		try {
			jwtParser.parseClaimsJws(claimsJws);
			return true;
		} catch (JwtException e) {
		}

		return false;
	}
}