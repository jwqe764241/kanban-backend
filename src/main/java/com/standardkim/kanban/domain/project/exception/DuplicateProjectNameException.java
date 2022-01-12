package com.standardkim.kanban.domain.project.exception;

import com.standardkim.kanban.global.exception.ErrorCode;
import com.standardkim.kanban.global.exception.InvalidValueException;

public class DuplicateProjectNameException extends InvalidValueException {
	public DuplicateProjectNameException(String message) {
		super(message, ErrorCode.DUPLICATE_PROJECT_NAME);
	}
}
