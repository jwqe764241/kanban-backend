package com.standardkim.kanban.exception;

public class ResourceNotFoundException extends RuntimeException {
	private ErrorCode errorCode;

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
