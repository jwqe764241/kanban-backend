package com.standardkim.kanban.exception;

public class TokenNotProvidedException extends RuntimeException{
	public TokenNotProvidedException(String message) {
		super(message);
	}
}
