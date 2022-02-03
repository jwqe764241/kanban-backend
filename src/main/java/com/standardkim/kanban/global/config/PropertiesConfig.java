package com.standardkim.kanban.global.config;

import com.standardkim.kanban.global.config.property.InvitationProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {InvitationProperties.class})
public class PropertiesConfig {
}
