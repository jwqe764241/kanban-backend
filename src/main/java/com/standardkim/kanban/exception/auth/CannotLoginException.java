package com.standardkim.kanban.exception.auth;

import com.standardkim.kanban.exception.ErrorCode;

public class CannotLoginException extends RuntimeException {
	private ErrorCode errorCode;

	public CannotLoginException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
