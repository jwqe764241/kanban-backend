package com.standardkim.kanban.exception.auth;

import com.standardkim.kanban.exception.ErrorCode;

public class UnknownRefreshTokenException extends InvalidRefreshTokenException {
	public UnknownRefreshTokenException(String message) {
		super(message, ErrorCode.UNKNOWN_REFRESH_TOKEN);
	}
}
