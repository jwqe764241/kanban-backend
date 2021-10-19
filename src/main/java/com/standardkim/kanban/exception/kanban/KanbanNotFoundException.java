package com.standardkim.kanban.exception.kanban;

import com.standardkim.kanban.exception.EntityNotFoundException;
import com.standardkim.kanban.exception.ErrorCode;

public class KanbanNotFoundException extends EntityNotFoundException {
	public KanbanNotFoundException(String message) {
		super(message, ErrorCode.KANBAN_NOT_FOUND);
	}
}
