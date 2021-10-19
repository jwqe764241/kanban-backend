package com.standardkim.kanban.exception.project;

import com.standardkim.kanban.exception.EntityNotFoundException;
import com.standardkim.kanban.exception.ErrorCode;

public class ProjectNotFoundException extends EntityNotFoundException {
	public ProjectNotFoundException(String message) {
		super(message, ErrorCode.PROJECT_NOT_FOUND);
	}
}
