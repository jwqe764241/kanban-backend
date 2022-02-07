package com.standardkim.kanban.domain.projectmember.exception;

import com.standardkim.kanban.global.error.ErrorCode;
import com.standardkim.kanban.global.error.exception.EntityNotFoundException;

//Project role must be exist.
//If not exists, run data.sql in resources folder
public class ProjectRoleNotFoundException extends EntityNotFoundException {
	public ProjectRoleNotFoundException(String message) {
		super(message, ErrorCode.INTERNAL_SERVER_ERROR);
	}
}
