package com.standardkim.kanban.dto;

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
	public static class NewProjectRequest {
		@NotBlank
		@Size(min = 2, max = 50)
		private String name;

		@Size(max = 200)
		private String description;
	}
}
