package com.standardkim.kanban.domain.taskcolumn.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class ColumnAction {
	public enum ActionType {
		Insert, Update, Delete, Reorder
	}

	private final String target = "Column";
	protected ActionType actionType;
	protected Object payload;
}
