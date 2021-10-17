package com.standardkim.kanban.exception.auth;

import com.standardkim.kanban.exception.ErrorCode;

public class UnknownRefreshTokenException extends RuntimeException {
	private ErrorCode errorCode;

	public UnknownRefreshTokenException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
