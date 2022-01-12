package com.standardkim.kanban.domain.taskcolumn.exception;

import com.standardkim.kanban.global.exception.EntityNotFoundException;
import com.standardkim.kanban.global.exception.ErrorCode;

public class TaskColumnNotFoundException extends EntityNotFoundException {
	public TaskColumnNotFoundException(String message) {
		super(message, ErrorCode.TASK_COLUMN_NOT_FOUND);
	}
}
