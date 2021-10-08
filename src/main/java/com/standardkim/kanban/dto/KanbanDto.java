package com.standardkim.kanban.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class KanbanDto {
	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class CreateKanbanParam {
		@NotBlank
		@Size(min = 2, max = 50)
		private String name;
		
		@Size(max = 200)
		private String description;
	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class UpdateKanbanParam {
		@NotBlank
		@Size(min = 2, max = 50)
		private String name;
		
		@Size(max = 200)
		private String description;
	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class KanbanDetail {
		private Long projectId;
		private Long sequenceId;
		private String name;
		private String description;
		private LocalDateTime registerDate;
	}
}
