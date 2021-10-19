package com.standardkim.kanban.config.handler;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.standardkim.kanban.dto.ErrorResponseDto.ErrorResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailedHandler implements AuthenticationEntryPoint{
	private ObjectMapper objectMapper;
	
	@Value("${config.allowed-origins}")
	String[] allowedOrigins;

	private String flattenAllowedOrigins;

	@PostConstruct
	public void init() {
		flattenAllowedOrigins = String.join(",", allowedOrigins);
	}

	public AuthenticationFailedHandler() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());
		this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.setHeader("Access-Control-Allow-Origin", flattenAllowedOrigins);
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(401);
		response.getWriter().write(buildResponse());
	}
	
	private String buildResponse()
	{
		ErrorResponse response = ErrorResponse.builder()
			.message("access token is invalid. check your authentication")
			.build();

		try
		{
			return objectMapper.writeValueAsString(response);
		}
		catch (JsonProcessingException e)
		{
			return "";
		}
	}
}
