package com.standardkim.kanban.domain.taskcolumn.exception;

import com.standardkim.kanban.global.error.ErrorCode;
import com.standardkim.kanban.global.error.exception.InvalidValueException;

public class DuplicateTaskColumnNameException extends InvalidValueException {
	public DuplicateTaskColumnNameException(String message) {
		super(message, ErrorCode.DUPLICATE_TASK_COLUMN_NAME);
	}
}
