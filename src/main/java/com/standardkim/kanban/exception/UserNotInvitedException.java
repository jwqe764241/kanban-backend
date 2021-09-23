package com.standardkim.kanban.exception;

public class UserNotInvitedException extends ResourceNotFoundException{
	public UserNotInvitedException(String message) {
		super(message);
	}
}
