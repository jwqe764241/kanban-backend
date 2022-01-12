package com.standardkim.kanban.domain.kanban.exception;

import com.standardkim.kanban.global.exception.EntityNotFoundException;
import com.standardkim.kanban.global.exception.ErrorCode;

public class KanbanNotFoundException extends EntityNotFoundException {
	public KanbanNotFoundException(String message) {
		super(message, ErrorCode.KANBAN_NOT_FOUND);
	}
}
