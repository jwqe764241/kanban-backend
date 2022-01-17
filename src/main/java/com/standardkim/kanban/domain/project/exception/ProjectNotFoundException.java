package com.standardkim.kanban.domain.project.exception;

import com.standardkim.kanban.global.error.ErrorCode;
import com.standardkim.kanban.global.error.exception.EntityNotFoundException;

public class ProjectNotFoundException extends EntityNotFoundException {
	public ProjectNotFoundException(String message) {
		super(message, ErrorCode.PROJECT_NOT_FOUND);
	}
}
