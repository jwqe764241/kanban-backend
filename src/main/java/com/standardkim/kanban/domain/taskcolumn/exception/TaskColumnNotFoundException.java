package com.standardkim.kanban.domain.taskcolumn.exception;

import com.standardkim.kanban.global.error.ErrorCode;
import com.standardkim.kanban.global.error.exception.EntityNotFoundException;

public class TaskColumnNotFoundException extends EntityNotFoundException {
	public TaskColumnNotFoundException(String message) {
		super(message, ErrorCode.TASK_COLUMN_NOT_FOUND);
	}
}
