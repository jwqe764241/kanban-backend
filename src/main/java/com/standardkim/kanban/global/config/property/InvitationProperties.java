package com.standardkim.kanban.global.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("config.invitation")
public final class InvitationProperties {
	private final String acceptInvitationUrl;
	private final String fromAddress;
}
