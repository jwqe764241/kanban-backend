package com.standardkim.kanban.domain.auth.exception;

import com.standardkim.kanban.global.exception.ErrorCode;

public class UnknownRefreshTokenException extends InvalidRefreshTokenException {
	public UnknownRefreshTokenException(String message) {
		super(message, ErrorCode.UNKNOWN_REFRESH_TOKEN);
	}
}
