package com.standardkim.kanban.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
		private String error;
		private String message;
		@Builder.Default
		private LocalDateTime date = LocalDateTime.now();
		@Builder.Default
		private List<Object> datas = new ArrayList<Object>();
	}
}
