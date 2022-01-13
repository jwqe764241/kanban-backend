package com.standardkim.kanban.domain.projectmember.exception;

import com.standardkim.kanban.global.error.ErrorCode;
import com.standardkim.kanban.global.error.exception.EntityNotFoundException;

public class ProjectMemberNotFoundException extends EntityNotFoundException {
	public ProjectMemberNotFoundException(String message) {
		super(message, ErrorCode.PROJECT_MEMBER_NOT_FOUND);
	}
}
