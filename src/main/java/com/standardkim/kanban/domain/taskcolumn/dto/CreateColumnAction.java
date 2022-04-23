package com.standardkim.kanban.domain.taskcolumn.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateColumnAction extends ColumnAction {
	@Builder
	public CreateColumnAction(TaskColumnDetail payload) {
		super(ActionType.Insert, payload);
	}

	public static CreateColumnAction of(TaskColumnDetail taskColumnDetail) {
		return CreateColumnAction.builder()
			.payload(taskColumnDetail)
			.build();
	}
}
