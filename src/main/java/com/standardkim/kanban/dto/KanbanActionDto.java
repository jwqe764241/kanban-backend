package com.standardkim.kanban.dto;

import com.standardkim.kanban.dto.TaskColumnDto.TaskColumnDetail;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class KanbanActionDto {
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static abstract class KanbanAction {
		public enum ActionType {
			Insert, Update, Delete
		}

		public enum Target {
			Column, Task
		}

		protected ActionType actionType;
		protected Target target;
		protected Object payload;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class CreateColumnAction extends KanbanAction {
		@Builder
		public CreateColumnAction(TaskColumnDetail payload) {
			super(ActionType.Insert, Target.Column, payload);
		}

		public static CreateColumnAction from(TaskColumnDetail payload) {
			return CreateColumnAction.builder()
				.payload(payload)
				.build();
		}
	}
}
