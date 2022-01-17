package com.standardkim.kanban.global.error.exception;

import com.standardkim.kanban.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException{
	public EntityNotFoundException(String message) {
		super(message, ErrorCode.ENTITY_NOT_FOUND);
	}

	public EntityNotFoundException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}
}
