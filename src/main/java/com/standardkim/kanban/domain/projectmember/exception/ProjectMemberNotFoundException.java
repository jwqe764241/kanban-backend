package com.standardkim.kanban.domain.projectmember.exception;

import com.standardkim.kanban.global.exception.EntityNotFoundException;
import com.standardkim.kanban.global.exception.ErrorCode;

public class ProjectMemberNotFoundException extends EntityNotFoundException {
	public ProjectMemberNotFoundException(String message) {
		super(message, ErrorCode.PROJECT_MEMBER_NOT_FOUND);
	}
}
