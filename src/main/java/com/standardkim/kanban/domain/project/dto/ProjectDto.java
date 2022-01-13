package com.standardkim.kanban.domain.project.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProjectDto {
	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class CreateProjectParam {
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
	public static class ProjectDetail {
		private Long id;
		private String name;
		private String description;
		private String registerUsername;
		private LocalDateTime createdAt;
	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class UpdateProjectParam {
		@NotBlank
		@Size(min = 2, max = 50)
		private String name;
	}
}
