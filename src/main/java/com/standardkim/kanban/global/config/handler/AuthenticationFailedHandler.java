package com.standardkim.kanban.global.config.handler;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.standardkim.kanban.global.config.property.ConfigProperties;
import com.standardkim.kanban.global.error.ErrorCode;
import com.standardkim.kanban.global.error.ErrorResponse;
import com.standardkim.kanban.global.util.ErrorResponseJsonConverter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationFailedHandler implements AuthenticationEntryPoint{
	private ErrorResponseJsonConverter errorResponseJsonConverter = new ErrorResponseJsonConverter();
	
	private final ConfigProperties configProperties;

	private String flattenAllowedOrigins;

	@PostConstruct
	public void init() {
		flattenAllowedOrigins = String.join(",", configProperties.getAllowedOrigins());
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_ACCESS_TOKEN);
		String responseJson = errorResponseJsonConverter.convert(errorResponse).orElse("");
		response.setHeader("Access-Control-Allow-Origin", flattenAllowedOrigins);
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(401);
		response.getWriter().write(responseJson);
	}
}
