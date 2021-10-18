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
		private String error;
		private String detail;
		@Builder.Default
		private LocalDateTime timestamp = LocalDateTime.now();
		@JsonInclude(JsonInclude.Include.NON_NULL)
		private Object data;

		public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
			return ResponseEntity.status(errorCode.getHttpStatus())
				.body(ErrorResponse.builder()
						.error(errorCode.getCode())
						.detail(errorCode.getDetail())
						.build()
				);
		}

		public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode, Object data) {
			return ResponseEntity.status(errorCode.getHttpStatus())
				.body(ErrorResponse.builder()
						.error(errorCode.getCode())
						.detail(errorCode.getDetail())
						.data(data)
						.build()
				);
		}
	}
}
