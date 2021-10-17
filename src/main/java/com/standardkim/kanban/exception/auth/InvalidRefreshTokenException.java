package com.standardkim.kanban.exception.auth;

import com.standardkim.kanban.exception.ErrorCode;

public class InvalidRefreshTokenException extends RuntimeException {
	private ErrorCode errorCode;

	public InvalidRefreshTokenException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
