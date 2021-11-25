package com.standardkim.kanban.exception.task;

import com.standardkim.kanban.exception.EntityNotFoundException;
import com.standardkim.kanban.exception.ErrorCode;

public class TaskNotFoundException extends EntityNotFoundException {
	public TaskNotFoundException(String message) {
		super(message, ErrorCode.TASK_NOT_FOUND);
	}
}
