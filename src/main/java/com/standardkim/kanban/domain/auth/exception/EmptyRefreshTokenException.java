package com.standardkim.kanban.domain.auth.exception;

import com.standardkim.kanban.global.error.ErrorCode;

public class EmptyRefreshTokenException extends InvalidRefreshTokenException {
	public EmptyRefreshTokenException(String message) {
		super(message, ErrorCode.EMPTY_REFRESH_TOKEN);
	}
}
