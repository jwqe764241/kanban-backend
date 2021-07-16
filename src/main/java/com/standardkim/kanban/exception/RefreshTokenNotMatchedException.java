package com.standardkim.kanban.exception;

public class RefreshTokenNotMatchedException extends RuntimeException {
	public RefreshTokenNotMatchedException(String message) {
		super(message);
	}
}
