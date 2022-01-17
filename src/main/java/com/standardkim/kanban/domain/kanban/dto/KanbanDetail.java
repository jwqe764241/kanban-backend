package com.standardkim.kanban.domain.kanban.dto;

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
public class KanbanDetail {
	private Long projectId;
	private Long sequenceId;
	private String name;
	private String description;
	private LocalDateTime createdAt;
}
