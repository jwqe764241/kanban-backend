package com.standardkim.kanban.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.standardkim.kanban.dto.TaskColumnDto.TaskColumnDetail;
import com.standardkim.kanban.dto.TaskDto.TaskDetail;

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

		public static CreateColumnAction from(TaskColumnDetail taskColumnDetail) {
			return CreateColumnAction.builder()
				.payload(taskColumnDetail)
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

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class UpdateColumnAction extends KanbanAction {
		@Builder
		public UpdateColumnAction(Object payload) {
			super(ActionType.Update, Target.Column, payload);
		}

		public static UpdateColumnAction from(TaskColumnDetail taskColumnDetail) {
			return UpdateColumnAction.builder()
				.payload(taskColumnDetail)
				.build();
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class CreateTaskAction extends KanbanAction {
		@Builder
		public CreateTaskAction(Object payload) {
			super(ActionType.Insert, Target.Task, payload);
		}

		public static CreateTaskAction from(List<TaskDetail> updatedTaskDetails) {
			return CreateTaskAction.builder()
				.payload(updatedTaskDetails)
				.build();
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class DeleteTaskAction extends KanbanAction {
		@Builder
		public DeleteTaskAction(Object payload) {
			super(ActionType.Delete, Target.Task, payload);
		}

		public static DeleteTaskAction from(Long deletedTaskId, TaskDetail updatedTaskDetail) {
			Map<String, Object> payload = new HashMap<>();
			payload.put("deletedTaskId", deletedTaskId);
			payload.put("updatedTask", updatedTaskDetail);

			return DeleteTaskAction.builder()
				.payload(payload)
				.build();
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ReorderTaskAction extends KanbanAction {
		@Builder
		public ReorderTaskAction(Object payload) {
			super(ActionType.Reorder, Target.Task, payload);
		}

		public static ReorderTaskAction from(List<TaskDetail> updatedTaskDetails) {
			return ReorderTaskAction.builder()
				.payload(updatedTaskDetails)
				.build();
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class UpdateTaskAction extends KanbanAction {
		@Builder
		public UpdateTaskAction(Object payload) {
			super(ActionType.Update, Target.Task, payload);
		}

		public static UpdateTaskAction from(TaskDetail updatedTaskDetail) {
			return UpdateTaskAction.builder()
				.payload(updatedTaskDetail)
				.build();
		}
	}
}