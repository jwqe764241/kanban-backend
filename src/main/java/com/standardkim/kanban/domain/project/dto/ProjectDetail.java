package com.standardkim.kanban.domain.project.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProjectDetail {
	private Long id;
	private String name;
	private String description;
	private String registerUsername;
	private LocalDateTime createdAt;
}
