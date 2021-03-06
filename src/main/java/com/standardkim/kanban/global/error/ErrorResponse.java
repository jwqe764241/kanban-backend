package com.standardkim.kanban.global.error;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.http.ResponseEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ErrorResponse {
	private String code;
	private String message;
	@Builder.Default
	private LocalDateTime timestamp = LocalDateTime.now();
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object data;

	public static ErrorResponse of(ErrorCode errorCode) {
		return ErrorResponse.builder()
			.code(errorCode.getCode())
			.message(errorCode.getMessage())
			.build();
	}

	public static ErrorResponse of(ErrorCode errorCode, Object data) {
		return ErrorResponse.builder()
			.code(errorCode.getCode())
			.message(errorCode.getMessage())
			.data(data)
			.build();
	}

	public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(of(errorCode));
	}

	public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode, Object data) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(of(errorCode, data));
	}
}
