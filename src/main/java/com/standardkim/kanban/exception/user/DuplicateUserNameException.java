package com.standardkim.kanban.exception.user;

import com.standardkim.kanban.exception.ErrorCode;
import com.standardkim.kanban.exception.InvalidValueException;

public class DuplicateUserNameException extends InvalidValueException {
	public DuplicateUserNameException(String message) {
		super(message, ErrorCode.DUPLICATE_USER_NAME);
	}
}
