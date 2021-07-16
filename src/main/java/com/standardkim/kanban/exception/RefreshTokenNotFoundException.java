package com.standardkim.kanban.exception;

public class RefreshTokenNotFoundException extends RuntimeException {
	public RefreshTokenNotFoundException(String message) { 
		super(message);
	}
}
