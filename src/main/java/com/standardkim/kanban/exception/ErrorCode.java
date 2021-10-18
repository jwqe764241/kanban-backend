package com.standardkim.kanban.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	AUTH_EXPIRED_REFRESH_TOKEN("AUTH-001", HttpStatus.UNAUTHORIZED, "refresh token was expired"),
	AUTH_UNKNOWN_REFRESH_TOKEN("AUTH-002", HttpStatus.UNAUTHORIZED, "refresh token was unknown"),
	AUTH_REFRESH_TOKEN_NOT_PROVIDED("AUTH-003", HttpStatus.BAD_REQUEST, "refresh token must be provided"),
	AUTH_INVALID_REFRESH_TOKEN("AUTH-004", HttpStatus.BAD_REQUEST, " refresh token was invalid"),
	AUTH_INCORRECT_USERNAME_OR_PASSWORD("AUTH-005", HttpStatus.UNAUTHORIZED, "login failed. check username of password"),

	COMMON_RESOURCE_NOT_FOUND("COMMON-001", HttpStatus.NOT_FOUND, "resource not found");

	private final String code;
	private final HttpStatus httpStatus;
	private final String detail;

	ErrorCode(String code, HttpStatus httpStatus, String detail) {
		this.code = code;
		this.httpStatus = httpStatus;
		this.detail = detail;
	}

	public String getCode() {
		return code;
	}
	
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getDetail() {
		return detail;
	}
}
