package com.standardkim.kanban.domain.project.exception;

import com.standardkim.kanban.global.exception.EntityNotFoundException;
import com.standardkim.kanban.global.exception.ErrorCode;

public class ProjectNotFoundException extends EntityNotFoundException {
	public ProjectNotFoundException(String message) {
		super(message, ErrorCode.PROJECT_NOT_FOUND);
	}
}
