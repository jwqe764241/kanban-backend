package com.standardkim.kanban.domain.projectmember.exception;

import com.standardkim.kanban.global.exception.EntityNotFoundException;
import com.standardkim.kanban.global.exception.ErrorCode;

public class InvitationNotFoundException extends EntityNotFoundException {
	public InvitationNotFoundException(String message) {
		super(message, ErrorCode.INVITATION_NOT_FOUND);
	}
}
