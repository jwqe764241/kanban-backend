package com.standardkim.kanban.exception.auth;

import com.standardkim.kanban.exception.ErrorCode;

public class ExpiredRefreshTokenException extends InvalidRefreshTokenException {
	public ExpiredRefreshTokenException(String message) {
		super(message, ErrorCode.EXPIRED_REFRESH_TOKEN);
	}
}
