package com.standardkim.kanban.domain.auth.exception;

import com.standardkim.kanban.global.error.ErrorCode;

public class ExpiredRefreshTokenException extends InvalidRefreshTokenException {
	public ExpiredRefreshTokenException(String message) {
		super(message, ErrorCode.EXPIRED_REFRESH_TOKEN);
	}
}
