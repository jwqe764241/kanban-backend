package com.standardkim.kanban.domain.task.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateTaskAction extends TaskAction {
	@Builder
	public UpdateTaskAction(Object payload) {
		super(ActionType.Update, payload);
	}

	public static UpdateTaskAction of(TaskDetail updatedTaskDetail) {
		return UpdateTaskAction.builder()
			.payload(updatedTaskDetail)
			.build();
	}
}
