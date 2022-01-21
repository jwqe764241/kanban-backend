package com.standardkim.kanban.domain.user.exception;

import com.standardkim.kanban.global.error.ErrorCode;
import com.standardkim.kanban.global.error.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
	public UserNotFoundException(String message) {
		super(message, ErrorCode.USER_NOT_FOUND);
	}
}
