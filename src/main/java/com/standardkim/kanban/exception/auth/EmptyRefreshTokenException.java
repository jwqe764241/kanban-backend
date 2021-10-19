package com.standardkim.kanban.exception.auth;

import com.standardkim.kanban.exception.ErrorCode;

public class EmptyRefreshTokenException extends InvalidRefreshTokenException {
	public EmptyRefreshTokenException(String message) {
		super(message, ErrorCode.EMPTY_REFRESH_TOKEN);
	}
}
