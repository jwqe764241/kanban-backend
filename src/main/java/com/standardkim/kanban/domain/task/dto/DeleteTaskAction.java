package com.standardkim.kanban.domain.task.dto;

import java.util.HashMap;
import java.util.Map;

import com.standardkim.kanban.global.dto.KanbanAction;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteTaskAction extends KanbanAction {
	@Builder
	public DeleteTaskAction(Object payload) {
		super(ActionType.Delete, Target.Task, payload);
	}

	public static DeleteTaskAction of(Long deletedTaskId, TaskDetail updatedTaskDetail) {
		Map<String, Object> payload = new HashMap<>();
		payload.put("deletedTaskId", deletedTaskId);
		payload.put("updatedTask", updatedTaskDetail);

		return DeleteTaskAction.builder()
			.payload(payload)
			.build();
	}
}
