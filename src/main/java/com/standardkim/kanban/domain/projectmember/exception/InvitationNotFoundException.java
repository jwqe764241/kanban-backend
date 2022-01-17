package com.standardkim.kanban.domain.projectmember.exception;

import com.standardkim.kanban.global.error.ErrorCode;
import com.standardkim.kanban.global.error.exception.EntityNotFoundException;

public class InvitationNotFoundException extends EntityNotFoundException {
	public InvitationNotFoundException(String message) {
		super(message, ErrorCode.INVITATION_NOT_FOUND);
	}
}
