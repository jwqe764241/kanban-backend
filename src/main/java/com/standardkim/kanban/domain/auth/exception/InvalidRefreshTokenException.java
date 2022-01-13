package com.standardkim.kanban.domain.auth.exception;

import com.standardkim.kanban.global.error.ErrorCode;
import com.standardkim.kanban.global.error.exception.InvalidValueException;

public class InvalidRefreshTokenException extends InvalidValueException {
	public InvalidRefreshTokenException(String message) {
		super(message, ErrorCode.INVALID_REFRESH_TOKEN);
	}

	public InvalidRefreshTokenException(String message, Throwable cause) {
		super(message, cause, ErrorCode.INVALID_REFRESH_TOKEN);
	}

	public InvalidRefreshTokenException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}
}
