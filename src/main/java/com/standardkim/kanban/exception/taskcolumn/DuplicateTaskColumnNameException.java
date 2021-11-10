package com.standardkim.kanban.exception.taskcolumn;

import com.standardkim.kanban.exception.ErrorCode;
import com.standardkim.kanban.exception.InvalidValueException;

public class DuplicateTaskColumnNameException extends InvalidValueException {
	public DuplicateTaskColumnNameException(String message) {
		super(message, ErrorCode.DUPLICATE_TASK_COLUMN_NAME);
	}
}
