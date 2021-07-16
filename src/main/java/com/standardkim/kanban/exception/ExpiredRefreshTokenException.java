package com.standardkim.kanban.exception;

public class ExpiredRefreshTokenException extends RuntimeException {
	public ExpiredRefreshTokenException(String message) {
		super(message);
	}
}