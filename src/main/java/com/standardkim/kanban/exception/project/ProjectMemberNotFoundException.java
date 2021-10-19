package com.standardkim.kanban.exception.project;

import com.standardkim.kanban.exception.EntityNotFoundException;
import com.standardkim.kanban.exception.ErrorCode;

public class ProjectMemberNotFoundException extends EntityNotFoundException {
	public ProjectMemberNotFoundException(String message) {
		super(message, ErrorCode.PROJECT_MEMBER_NOT_FOUND);
	}
}
