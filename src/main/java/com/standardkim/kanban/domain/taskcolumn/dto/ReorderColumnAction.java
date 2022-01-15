package com.standardkim.kanban.domain.taskcolumn.dto;

import java.util.List;

import com.standardkim.kanban.global.dto.KanbanAction;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReorderColumnAction extends KanbanAction {
	@Builder
	public ReorderColumnAction(Object payload) {
		super(ActionType.Reorder, Target.Column, payload);
	}

	public static ReorderColumnAction from(List<TaskColumnDetail> updatedTaskColumnDetails) {
		return ReorderColumnAction.builder()
			.payload(updatedTaskColumnDetails)
			.build();
	}
}
