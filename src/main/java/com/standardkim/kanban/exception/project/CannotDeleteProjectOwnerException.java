package com.standardkim.kanban.exception.project;

import com.standardkim.kanban.exception.ErrorCode;
import com.standardkim.kanban.exception.InvalidValueException;

public class CannotDeleteProjectOwnerException extends InvalidValueException {
	public CannotDeleteProjectOwnerException(String message) {
		super(message, ErrorCode.CANNOT_DELETE_PROJECT_OWNER);
	}
}
