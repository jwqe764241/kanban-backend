package com.standardkim.kanban.global.config.interceptor;

import com.standardkim.kanban.global.auth.dto.AuthorizationHeader;
import com.standardkim.kanban.global.util.JwtTokenProvider;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketConnectInterceptor implements ChannelInterceptor {
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		
		if(accessor.getMessageType().equals(SimpMessageType.CONNECT)) {
			String rawToken = accessor.getFirstNativeHeader("Authorization");
			AuthorizationHeader authHeader = null;
			try {
				authHeader = parseAuthorizationHeader(rawToken);
			} catch (Exception e) {
				throw new MessagingException("invalid access token");
			}

			if(authHeader != null && authHeader.isValid() 
				&& !jwtTokenProvider.isValid(authHeader.getCredentials())) {
				throw new MessagingException("invalid access token");
			}
		}

		return message;
	}

	private AuthorizationHeader parseAuthorizationHeader(String token) {
		String[] tokenized = token.split(" ");

		if(tokenized.length != 2) {
			throw new IllegalArgumentException("invalid header data");
		}

		AuthorizationHeader header = AuthorizationHeader.builder()
			.type(tokenized[0])
			.credentials(tokenized[1])
			.build();

		return header;
	}
}
