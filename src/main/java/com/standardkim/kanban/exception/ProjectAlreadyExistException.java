package com.standardkim.kanban.exception;

public class ProjectAlreadyExistException extends RuntimeException {
	public ProjectAlreadyExistException(String message) {
		super(message);
	}
}
