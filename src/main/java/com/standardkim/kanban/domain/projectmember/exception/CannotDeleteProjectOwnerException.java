package com.standardkim.kanban.domain.projectmember.exception;

import com.standardkim.kanban.global.error.ErrorCode;
import com.standardkim.kanban.global.error.exception.InvalidValueException;

public class CannotDeleteProjectOwnerException extends InvalidValueException {
	public CannotDeleteProjectOwnerException(String message) {
		super(message, ErrorCode.CANNOT_DELETE_PROJECT_OWNER);
	}
}
