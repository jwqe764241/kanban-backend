package com.standardkim.kanban.exception.user;

import com.standardkim.kanban.exception.EntityNotFoundException;
import com.standardkim.kanban.exception.ErrorCode;

public class UserNotFoundException extends EntityNotFoundException {
	public UserNotFoundException(String message) {
		super(message, ErrorCode.USER_NOT_FOUND);
	}
}
