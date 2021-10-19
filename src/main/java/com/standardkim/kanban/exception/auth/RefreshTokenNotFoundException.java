package com.standardkim.kanban.exception.auth;

import com.standardkim.kanban.exception.EntityNotFoundException;
import com.standardkim.kanban.exception.ErrorCode;

public class RefreshTokenNotFoundException extends EntityNotFoundException {
	public RefreshTokenNotFoundException(String message) {
		super(message, ErrorCode.ENTITY_NOT_FOUND);
	}
}
