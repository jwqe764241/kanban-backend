package com.standardkim.kanban.exception.taskcolumn;

import com.standardkim.kanban.exception.EntityNotFoundException;
import com.standardkim.kanban.exception.ErrorCode;

public class TaskColumnNotFoundException extends EntityNotFoundException {
	public TaskColumnNotFoundException(String message) {
		super(message, ErrorCode.TASK_COLUMN_NOT_FOUND);
	}
}
