package com.standardkim.kanban.config;

import com.standardkim.kanban.config.interceptor.WebSocketConnectInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	private final WebSocketConnectInterceptor webSocketConnectInterceptor;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic/kanban");
	}

	@Override
    public void configureClientInboundChannel(ChannelRegistration registry) {
		registry.interceptors(webSocketConnectInterceptor);
    }

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/kanban-event")
			.setAllowedOrigins("http://localhost:3000")
			.withSockJS();
	}
}
