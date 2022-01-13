package com.standardkim.kanban.domain.task.exception;

import com.standardkim.kanban.global.error.ErrorCode;
import com.standardkim.kanban.global.error.exception.EntityNotFoundException;

public class TaskNotFoundException extends EntityNotFoundException {
	public TaskNotFoundException(String message) {
		super(message, ErrorCode.TASK_NOT_FOUND);
	}
}
