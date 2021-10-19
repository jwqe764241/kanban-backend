package com.standardkim.kanban.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.User;

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

		public Project toEntity(User registerUser) {
			return Project.builder()
				.name(getName())
				.description(getDescription())
				.registerUser(registerUser)
				.build();
		}
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
		private LocalDateTime registerDate;
	}
}
