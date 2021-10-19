package com.standardkim.kanban.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.standardkim.kanban.exception.ErrorCode;

import org.springframework.http.ResponseEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ErrorResponseDto {
	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class ErrorResponse {
		private String code;
		private String message;
		@Builder.Default
		private LocalDateTime timestamp = LocalDateTime.now();
		@JsonInclude(JsonInclude.Include.NON_NULL)
		private Object data;

		public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
			return ResponseEntity.status(errorCode.getHttpStatus())
				.body(ErrorResponse.builder()
						.code(errorCode.getCode())
						.message(errorCode.getMessage())
						.build()
				);
		}

		public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode, Object data) {
			return ResponseEntity.status(errorCode.getHttpStatus())
				.body(ErrorResponse.builder()
						.code(errorCode.getCode())
						.message(errorCode.getMessage())
						.data(data)
						.build()
				);
		}
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class FieldValidationError {
		private String fieldName;
		private Object rejectedValue;
		private String message;
	}
}
