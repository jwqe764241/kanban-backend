package com.standardkim.kanban.exception;

public class LoginAlreadyInUse extends RuntimeException {
	public LoginAlreadyInUse(String message) {
		super(message);
	}
}
