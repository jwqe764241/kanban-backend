package com.standardkim.kanban.domain.refreshtoken.exception;

import com.standardkim.kanban.global.exception.EntityNotFoundException;
import com.standardkim.kanban.global.exception.ErrorCode;

public class RefreshTokenNotFoundException extends EntityNotFoundException {
	public RefreshTokenNotFoundException(String message) {
		super(message, ErrorCode.ENTITY_NOT_FOUND);
	}
}
