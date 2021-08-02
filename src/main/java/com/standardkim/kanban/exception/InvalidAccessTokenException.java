package com.standardkim.kanban.exception;

public class InvalidAccessTokenException extends RuntimeException{
	public InvalidAccessTokenException(String message) {
		super(message);
	}
}
