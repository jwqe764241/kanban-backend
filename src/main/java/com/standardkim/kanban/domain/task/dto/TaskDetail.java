package com.standardkim.kanban.domain.task.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TaskDetail {
	private Long id;
	private Long prevId;
	private Long taskColumnId;
	private String text;
}
