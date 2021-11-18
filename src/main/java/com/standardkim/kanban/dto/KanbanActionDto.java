package com.standardkim.kanban.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			Insert, Update, Delete, Reorder
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

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class DeleteColumnAction extends KanbanAction {
		@Builder
		public DeleteColumnAction(Object payload) {
			super(ActionType.Delete, Target.Column, payload);
		}

		public static DeleteColumnAction from(Long deletedColumnId, TaskColumnDetail updatedColumn) {
			Map<String, Object> payload = new HashMap<>();
			payload.put("deletedColumnId", deletedColumnId);
			payload.put("updatedColumn", updatedColumn);

			return DeleteColumnAction.builder()
				.payload(payload)
				.build();
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ReorderColumnAction extends KanbanAction {
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
}
