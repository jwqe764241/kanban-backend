package com.standardkim.kanban.global.auth.exception;

import com.standardkim.kanban.global.error.ErrorCode;

public class UnknownRefreshTokenException extends InvalidRefreshTokenException {
	public UnknownRefreshTokenException(String message) {
		super(message, ErrorCode.UNKNOWN_REFRESH_TOKEN);
	}
}
