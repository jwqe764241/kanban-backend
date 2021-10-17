package com.standardkim.kanban.exception.auth;

import com.standardkim.kanban.exception.ErrorCode;

public class ExpiredRefreshTokenException extends RuntimeException {
	private ErrorCode errorCode;

	public ExpiredRefreshTokenException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
