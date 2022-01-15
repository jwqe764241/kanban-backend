package com.standardkim.kanban.global.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class KanbanAction {
	public enum ActionType {
		Insert, Update, Delete, Reorder
	}

	public enum Target {
		Column, Task
	}

	protected ActionType actionType;
	protected Target target;
	protected Object payload;
}
