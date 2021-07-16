package com.standardkim.kanban.exception;

public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(String message) { 
		super(message);
	}	
}
