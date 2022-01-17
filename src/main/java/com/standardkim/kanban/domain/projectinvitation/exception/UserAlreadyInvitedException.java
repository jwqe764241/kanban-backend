package com.standardkim.kanban.domain.projectinvitation.exception;

import com.standardkim.kanban.global.error.ErrorCode;
import com.standardkim.kanban.global.error.exception.InvalidValueException;

public class UserAlreadyInvitedException extends InvalidValueException {
	public UserAlreadyInvitedException(String message) {
		super(message, ErrorCode.USER_ALREADY_INVITED);
	}
}
