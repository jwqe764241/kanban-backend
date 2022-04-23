package com.standardkim.kanban.domain.taskcolumn.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateColumnAction extends ColumnAction {
	@Builder
	public UpdateColumnAction(Object payload) {
		super(ActionType.Update, payload);
	}

	public static UpdateColumnAction of(TaskColumnDetail taskColumnDetail) {
		return UpdateColumnAction.builder()
			.payload(taskColumnDetail)
			.build();
	}
}
