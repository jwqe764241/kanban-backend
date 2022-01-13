package com.standardkim.kanban.domain.taskcolumn.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TaskColumnDto {
	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class CreateTaskColumnParam {
		@NotBlank
		@Size(min = 2, max = 50)
		private String name;
	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class ReorderTaskColumnParam {
		@NotNull
		Long columnId;
		Long prevColumnId;
	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class UpdateTaskColumnParam {
		@NotBlank
		@Size(min = 2, max = 50)
		private String name;
	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class TaskColumnDetail {
		private Long id;
		private Long prevId;
		private String name;
		private LocalDateTime createdAt;
	}
}
