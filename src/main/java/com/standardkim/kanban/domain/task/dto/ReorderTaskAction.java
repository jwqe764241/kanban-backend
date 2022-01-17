package com.standardkim.kanban.domain.task.dto;

import java.util.List;

import com.standardkim.kanban.global.dto.KanbanAction;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReorderTaskAction extends KanbanAction {
	@Builder
	public ReorderTaskAction(Object payload) {
		super(ActionType.Reorder, Target.Task, payload);
	}

	public static ReorderTaskAction of(List<TaskDetail> updatedTaskDetails) {
		return ReorderTaskAction.builder()
			.payload(updatedTaskDetails)
			.build();
	}
}
