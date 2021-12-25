package com.standardkim.kanban.config.handler;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.standardkim.kanban.dto.ErrorResponseDto.ErrorResponse;
import com.standardkim.kanban.exception.ErrorCode;
import com.standardkim.kanban.util.ErrorResponseJsonConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailedHandler implements AuthenticationEntryPoint{
	private ErrorResponseJsonConverter errorResponseJsonConverter = new ErrorResponseJsonConverter();
	
	@Value("${config.allowed-origins}")
	String[] allowedOrigins;

	private String flattenAllowedOrigins;

	@PostConstruct
	public void init() {
		flattenAllowedOrigins = String.join(",", allowedOrigins);
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		ErrorResponse errorResponse = ErrorResponse.from(ErrorCode.INVALID_ACCESS_TOKEN);
		String responseJson = errorResponseJsonConverter.convert(errorResponse).orElse("");
		response.setHeader("Access-Control-Allow-Origin", flattenAllowedOrigins);
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(401);
		response.getWriter().write(responseJson);
	}
}
