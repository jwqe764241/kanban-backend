package com.standardkim.kanban.exception.project;

import com.standardkim.kanban.exception.ErrorCode;
import com.standardkim.kanban.exception.InvalidValueException;

public class DuplicateProjectNameException extends InvalidValueException {
	public DuplicateProjectNameException(String message) {
		super(message, ErrorCode.DUPLICATE_PROJECT_NAME);
	}
}
