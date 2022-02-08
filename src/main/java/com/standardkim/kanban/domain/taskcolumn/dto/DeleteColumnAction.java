package com.standardkim.kanban.domain.taskcolumn.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteColumnAction extends ColumnAction {
	@Builder
	public DeleteColumnAction(Object payload) {
		super(ActionType.Delete, payload);
	}

	public static DeleteColumnAction of(Long deletedColumnId, TaskColumnDetail updatedColumn) {
		Map<String, Object> payload = new HashMap<>();
		payload.put("deletedColumnId", deletedColumnId);
		payload.put("updatedColumn", updatedColumn);

		return DeleteColumnAction.builder()
			.payload(payload)
			.build();
	}
}
