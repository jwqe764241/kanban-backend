package com.standardkim.kanban.exception;

import com.standardkim.kanban.dto.ErrorMessageDto.ErrorMessage;

public enum ErrorCode {
	;//TODO Add error code here

	private final Integer code;
	private final String message;
	private final String detail;

	ErrorCode(Integer code, String message, String detail) {
		this.code = code;
		this.message = message;
		this.detail = detail;
	}

	public Integer getCode() {
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
