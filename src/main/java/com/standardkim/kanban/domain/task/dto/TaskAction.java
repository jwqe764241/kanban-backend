package com.standardkim.kanban.domain.task.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class TaskAction {
	public enum ActionType {
		Insert, Update, Delete, Reorder
	}

	private final String target = "Task";
	protected ActionType actionType;
	protected Object payload;
}
