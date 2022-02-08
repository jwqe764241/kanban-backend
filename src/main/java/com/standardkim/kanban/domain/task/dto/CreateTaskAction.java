package com.standardkim.kanban.domain.task.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateTaskAction extends TaskAction {
	@Builder
	public CreateTaskAction(Object payload) {
		super(ActionType.Insert, payload);
	}

	public static CreateTaskAction of(List<TaskDetail> updatedTaskDetails) {
		return CreateTaskAction.builder()
			.payload(updatedTaskDetails)
			.build();
	}
}
