package com.standardkim.kanban.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	//auth
	EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-001", "refresh token is expired"),
	UNKNOWN_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-002", "refresh token is unknown"),
	EMPTY_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "AUTH-003", "refresh token is empty"),
	INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "AUTH-004", " refresh token is invalid"),
	INCORRECT_USERNAME_OR_PASSWORD(HttpStatus.UNAUTHORIZED, "AUTH-005", "username or password is incorrect."),
	//user
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "user not found"),
	DUPLICATE_USER_NAME(HttpStatus.CONFLICT, "USER-002", "duplicate user name"),
	//kanban
	KANBAN_NOT_FOUND(HttpStatus.NOT_FOUND, "KANBAN-001", "kanban not found"),
	//project
	PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT-001", "project not found"),
	PROJECT_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT-002", "project member not found"),
	INVITATION_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT-003", "user not invited"),
	USER_ALREADY_INVITED(HttpStatus.CONFLICT, "PROJECT-004", "user already invited"),
	DUPLICATE_PROJECT_NAME(HttpStatus.CONFLICT, "PROJECT-006", "duplicate project name"),
	CANNOT_DELETE_PROJECT_OWNER(HttpStatus.BAD_REQUEST, "PROJECT-005", "can't delete project owner"),
	//common
	ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON-001", "entity not found"),
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON-002", "input value is invalid"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-003", "internal server error");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

	ErrorCode(HttpStatus httpStatus, String code, String message) {
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
