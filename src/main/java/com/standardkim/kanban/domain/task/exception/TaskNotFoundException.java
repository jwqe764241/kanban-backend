package com.standardkim.kanban.domain.task.exception;

import com.standardkim.kanban.global.exception.EntityNotFoundException;
import com.standardkim.kanban.global.exception.ErrorCode;

public class TaskNotFoundException extends EntityNotFoundException {
	public TaskNotFoundException(String message) {
		super(message, ErrorCode.TASK_NOT_FOUND);
	}
}
