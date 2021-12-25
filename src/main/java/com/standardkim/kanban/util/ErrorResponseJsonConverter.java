package com.standardkim.kanban.util;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.standardkim.kanban.dto.ErrorResponseDto.ErrorResponse;

public class ErrorResponseJsonConverter {
	private ObjectMapper objectMapper;

	public ErrorResponseJsonConverter() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	public Optional<String> convert(ErrorResponse errorResponse) {
		try {
			String text = objectMapper.writeValueAsString(errorResponse);
			return Optional.of(text);
		}
		catch (JsonProcessingException e){
			return Optional.empty();
		}
	}
}
