package com.standardkim.kanban.exception.project;

import com.standardkim.kanban.exception.ErrorCode;
import com.standardkim.kanban.exception.InvalidValueException;

public class ProjectNameAlreadyExistsException extends InvalidValueException {
	public ProjectNameAlreadyExistsException(String message) {
		super(message, ErrorCode.PROJECT_NAME_ALREADY_EXISTS);
	}
}
