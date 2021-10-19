package com.standardkim.kanban.exception.project;

import com.standardkim.kanban.exception.EntityNotFoundException;
import com.standardkim.kanban.exception.ErrorCode;

public class InvitationNotFoundException extends EntityNotFoundException {
	public InvitationNotFoundException(String message) {
		super(message, ErrorCode.INVITATION_NOT_FOUND);
	}
}
