package com.standardkim.kanban.domain.auth.exception;

import com.standardkim.kanban.global.exception.ErrorCode;
import com.standardkim.kanban.global.exception.InvalidValueException;

public class CannotLoginException extends InvalidValueException {
	public CannotLoginException(String message) {
		super(message, ErrorCode.INCORRECT_USERNAME_OR_PASSWORD);
	}
}
