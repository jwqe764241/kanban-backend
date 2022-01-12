package com.standardkim.kanban.domain.projectmember.exception;

import com.standardkim.kanban.global.exception.ErrorCode;
import com.standardkim.kanban.global.exception.InvalidValueException;

public class CannotDeleteProjectOwnerException extends InvalidValueException {
	public CannotDeleteProjectOwnerException(String message) {
		super(message, ErrorCode.CANNOT_DELETE_PROJECT_OWNER);
	}
}
