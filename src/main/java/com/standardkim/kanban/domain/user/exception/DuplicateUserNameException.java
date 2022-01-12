package com.standardkim.kanban.domain.user.exception;

import com.standardkim.kanban.global.exception.ErrorCode;
import com.standardkim.kanban.global.exception.InvalidValueException;

public class DuplicateUserNameException extends InvalidValueException {
	public DuplicateUserNameException(String message) {
		super(message, ErrorCode.DUPLICATE_USER_NAME);
	}
}
