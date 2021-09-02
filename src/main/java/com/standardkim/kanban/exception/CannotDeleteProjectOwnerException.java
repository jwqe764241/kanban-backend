package com.standardkim.kanban.exception;

public class CannotDeleteProjectOwnerException extends RuntimeException{
	public CannotDeleteProjectOwnerException() {}

	public CannotDeleteProjectOwnerException(String message) {
		super(message);
	}
}
