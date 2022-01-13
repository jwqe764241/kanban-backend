package com.standardkim.kanban.domain.kanban.exception;

import com.standardkim.kanban.global.error.ErrorCode;
import com.standardkim.kanban.global.error.exception.EntityNotFoundException;

public class KanbanNotFoundException extends EntityNotFoundException {
	public KanbanNotFoundException(String message) {
		super(message, ErrorCode.KANBAN_NOT_FOUND);
	}
}
