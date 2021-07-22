package com.standardkim.kanban.exception;

public class LoginAlreadyInUseException extends RuntimeException {
	public LoginAlreadyInUseException(String message) {
		super(message);
	}
}
