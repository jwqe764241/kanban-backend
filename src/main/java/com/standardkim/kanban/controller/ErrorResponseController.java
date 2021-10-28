package com.standardkim.kanban.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import com.standardkim.kanban.dto.ErrorResponseDto.ErrorResponse;
import com.standardkim.kanban.dto.ErrorResponseDto.FieldValidationError;
import com.standardkim.kanban.exception.BusinessException;
import com.standardkim.kanban.exception.ErrorCode;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorResponseController {
	private HttpHeaders defaultHeaders;

	@PostConstruct
	public void init() {
		defaultHeaders = new HttpHeaders();
		defaultHeaders.add("Content-Type", "application/json; charset=UTF-8");
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
		Map<String, Object> errors = toFieldValidationError(e);
		return ErrorResponse.toResponseEntity(ErrorCode.VALIDATION_FAILED, errors);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
		return ErrorResponse.toResponseEntity(ErrorCode.ACCESS_DENIED);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
		return ErrorResponse.toResponseEntity(e.getErrorCode());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		e.printStackTrace();
		return ErrorResponse.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
	}

	private Map<String, Object> toFieldValidationError(MethodArgumentNotValidException e) {
		List<FieldError> list = e.getFieldErrors();
		Map<String, Object> result = new HashMap<>();
		for(FieldError fieldError : list) {
			FieldValidationError error = FieldValidationError.builder()
				.fieldName(fieldError.getField())
				.rejectedValue(fieldError.getRejectedValue())
				.message(fieldError.getDefaultMessage())
				.build();
			result.put(fieldError.getField(), error);
		}
		return result;
	}
}
