package com.standardkim.kanban.domain.task.dto;

import java.util.List;

import com.standardkim.kanban.global.dto.KanbanAction;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateTaskAction extends KanbanAction {
	@Builder
	public CreateTaskAction(Object payload) {
		super(ActionType.Insert, Target.Task, payload);
	}

	public static CreateTaskAction of(List<TaskDetail> updatedTaskDetails) {
		return CreateTaskAction.builder()
			.payload(updatedTaskDetails)
			.build();
	}
}
