package com.standardkim.kanban.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
	public ResourceAlreadyExistsException(String message) {
		super(message);
	}
}
