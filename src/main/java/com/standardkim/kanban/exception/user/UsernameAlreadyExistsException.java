package com.standardkim.kanban.exception.user;

import com.standardkim.kanban.exception.ErrorCode;
import com.standardkim.kanban.exception.InvalidValueException;

public class UsernameAlreadyExistsException extends InvalidValueException {
	public UsernameAlreadyExistsException(String message) {
		super(message, ErrorCode.USERNAME_ALREADY_EXISTS);
	}
}
