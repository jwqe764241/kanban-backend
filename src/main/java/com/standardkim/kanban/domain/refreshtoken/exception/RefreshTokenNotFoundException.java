package com.standardkim.kanban.domain.refreshtoken.exception;

import com.standardkim.kanban.global.error.ErrorCode;
import com.standardkim.kanban.global.error.exception.EntityNotFoundException;

public class RefreshTokenNotFoundException extends EntityNotFoundException {
	public RefreshTokenNotFoundException(String message) {
		super(message, ErrorCode.ENTITY_NOT_FOUND);
	}
}
