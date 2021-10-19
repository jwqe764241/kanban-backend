package com.standardkim.kanban.exception;

public class BusinessException extends RuntimeException {
	private ErrorCode errorCode;

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public BusinessException(String message, Throwable cause, ErrorCode errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
