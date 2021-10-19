package com.standardkim.kanban.exception.project;

import com.standardkim.kanban.exception.ErrorCode;
import com.standardkim.kanban.exception.InvalidValueException;

public class UserAlreadyInvitedException extends InvalidValueException {
	public UserAlreadyInvitedException(String message) {
		super(message, ErrorCode.USER_ALREADY_INVITED);
	}
}
