package com.standardkim.kanban.domain.project.exception;

import com.standardkim.kanban.global.error.ErrorCode;
import com.standardkim.kanban.global.error.exception.InvalidValueException;

public class DuplicateProjectNameException extends InvalidValueException {
	public DuplicateProjectNameException(String message) {
		super(message, ErrorCode.DUPLICATE_PROJECT_NAME);
	}
}
