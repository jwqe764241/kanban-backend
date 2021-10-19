package com.standardkim.kanban.exception;

public class InvalidValueException extends BusinessException {
	public InvalidValueException(String message) {
		super(message, ErrorCode.INVALID_INPUT_VALUE);
	}

	public InvalidValueException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

	public InvalidValueException(String message, Throwable cause) {
		super(message, cause, ErrorCode.INVALID_INPUT_VALUE);
	}

	public InvalidValueException(String message, Throwable cause, ErrorCode errorCode) {
		super(message, cause, errorCode);
	}
}
