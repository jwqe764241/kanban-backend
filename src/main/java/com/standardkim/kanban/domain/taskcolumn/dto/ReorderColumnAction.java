package com.standardkim.kanban.domain.taskcolumn.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReorderColumnAction extends ColumnAction {
	@Builder
	public ReorderColumnAction(Object payload) {
		super(ActionType.Reorder, payload);
	}

	public static ReorderColumnAction of(List<TaskColumnDetail> updatedTaskColumnDetails) {
		return ReorderColumnAction.builder()
			.payload(updatedTaskColumnDetails)
			.build();
	}
}
