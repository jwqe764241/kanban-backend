package com.standardkim.kanban.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ErrorMessageDto {
	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class ErrorMessage {
		private Integer code;
		private String message;
		private String detail;
		@Builder.Default
		private LocalDateTime date = LocalDateTime.now();
		private Object data;
	}
}
