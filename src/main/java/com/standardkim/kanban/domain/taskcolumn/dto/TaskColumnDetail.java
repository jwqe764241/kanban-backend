package com.standardkim.kanban.domain.taskcolumn.dto;

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
public class TaskColumnDetail {
	private Long id;
	private Long prevId;
	private String name;
	private LocalDateTime createdAt;
}
