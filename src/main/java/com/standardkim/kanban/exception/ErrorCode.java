package com.standardkim.kanban.exception;

import com.standardkim.kanban.dto.ErrorMessageDto.ErrorMessage;

public enum ErrorCode {
	AUTH_EXPIRED_REFRESH_TOKEN("AUTH-001", "failed to authentication", "refresh token was expired"),
	AUTH_UNKNOWN_REFRESH_TOKEN("AUTH-002", "failed to authentication", "refresh token was unknown"),
	AUTH_REFRESH_TOKEN_NOT_PROVIDED("AUTH-003", "failed to authentication", "refresh token must be provided"),
	AUTH_INVALID_REFRESH_TOKEN("AUTH-004", "failed to authenciation", "refresh token was invalid"),
	AUTH_INCORRECT_USERNAME_OR_PASSWORD("AUTH-005", "failed to authentication", "login failed. check username of password");

	private final String code;
	private final String message;
	private final String detail;

	ErrorCode(String code, String message, String detail) {
		this.code = code;
		this.message = message;
		this.detail = detail;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getDetail() {
		return detail;
	}

	public ErrorMessage toErrorMessage() {
		ErrorMessage errorMessage = ErrorMessage.builder()
			.code(code)
			.message(message)
			.detail(detail)
			.build();
		return errorMessage;
	}

	public ErrorMessage toErrorMessage(Object data) {
		ErrorMessage errorMessage = ErrorMessage.builder()
			.code(code)
			.message(message)
			.detail(detail)
			.data(data)
			.build();
		return errorMessage;
	}
}
