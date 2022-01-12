package com.standardkim.kanban.domain.user.exception;

import com.standardkim.kanban.global.exception.EntityNotFoundException;
import com.standardkim.kanban.global.exception.ErrorCode;

public class UserNotFoundException extends EntityNotFoundException {
	public UserNotFoundException(String message) {
		super(message, ErrorCode.USER_NOT_FOUND);
	}
}
