package com.standardkim.kanban.exception;

public class UserAlreadyInvitedException extends ResourceAlreadyExistsException {
	public UserAlreadyInvitedException(String message) {
		super(message);
	}
}
