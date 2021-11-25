package com.standardkim.kanban.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class CreateTaskParam {
		@NotBlank
		@Size(min = 2, max = 2048)
		private String text;
	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class ReorderTaskParam {
		private Long taskId;
		private Long prevTaskId;
	}
}
