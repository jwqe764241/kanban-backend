package com.standardkim.kanban.domain.taskcolumn.dto;

import com.standardkim.kanban.global.dto.KanbanAction;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateColumnAction extends KanbanAction {
	@Builder
	public CreateColumnAction(TaskColumnDetail payload) {
		super(ActionType.Insert, Target.Column, payload);
	}

	public static CreateColumnAction of(TaskColumnDetail taskColumnDetail) {
		return CreateColumnAction.builder()
			.payload(taskColumnDetail)
			.build();
	}
}
