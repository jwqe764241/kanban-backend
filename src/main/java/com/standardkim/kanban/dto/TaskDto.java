package com.standardkim.kanban.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TaskDto {
	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class TaskDetail {
		private Long id;
		private Long prevId;
		private Long taskColumnId;
		private String text;
	}
}
