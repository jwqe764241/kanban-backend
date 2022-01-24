package com.standardkim.kanban.global.auth.exception;

import com.standardkim.kanban.global.error.ErrorCode;
import com.standardkim.kanban.global.error.exception.InvalidValueException;

public class CannotSignInException extends InvalidValueException {
	public CannotSignInException(String message) {
		super(message, ErrorCode.INCORRECT_USERNAME_OR_PASSWORD);
	}
}
