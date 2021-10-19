package com.standardkim.kanban.exception.auth;

import com.standardkim.kanban.exception.ErrorCode;
import com.standardkim.kanban.exception.InvalidValueException;

public class CannotLoginException extends InvalidValueException {
	public CannotLoginException(String message) {
		super(message, ErrorCode.INCORRECT_USERNAME_OR_PASSWORD);
	}
}
