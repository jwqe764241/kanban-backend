package com.standardkim.kanban.global.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("config.authentication")
public final class AuthenticationProperties {
	private final String secretKey;
	private final String cookieDomain;
	private final String refreshTokenName;
	private final Long accessTokenTtl;
	private final Long refreshTokenTtl;
	private final Long wsTokenTtl;
}
