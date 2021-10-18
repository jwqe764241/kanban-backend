package com.standardkim.kanban.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import com.standardkim.kanban.dto.ErrorResponseDto.ErrorResponse;
import com.standardkim.kanban.exception.CannotDeleteProjectOwnerException;
import com.standardkim.kanban.exception.LoginAlreadyInUseException;
import com.standardkim.kanban.exception.ProjectAlreadyExistException;
import com.standardkim.kanban.exception.ResourceNotFoundException;
import com.standardkim.kanban.exception.UserAlreadyInvitedException;
import com.standardkim.kanban.exception.UserNotInvitedException;
import com.standardkim.kanban.exception.ValidationError;
import com.standardkim.kanban.exception.auth.CannotLoginException;
import com.standardkim.kanban.exception.auth.ExpiredRefreshTokenException;
import com.standardkim.kanban.exception.auth.InvalidRefreshTokenException;
import com.standardkim.kanban.exception.auth.UnknownRefreshTokenException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@ExceptionHandler(InvalidRefreshTokenException.class)
	public ResponseEntity<ErrorResponse> invalidRefreshToken(InvalidRefreshTokenException e) {
		return ErrorResponse.toResponseEntity(e.getErrorCode());
	}

	@ExceptionHandler(UnknownRefreshTokenException.class)
	public ResponseEntity<ErrorResponse> unknownRefreshToken(UnknownRefreshTokenException e) {
		return ErrorResponse.toResponseEntity(e.getErrorCode());
	}

	@ExceptionHandler(ExpiredRefreshTokenException.class)
	public ResponseEntity<ErrorResponse> expireRefreshToken(ExpiredRefreshTokenException e) {
		return ErrorResponse.toResponseEntity(e.getErrorCode());
	}

	@ExceptionHandler(CannotLoginException.class)
	public ResponseEntity<ErrorResponse> cannotLogin(CannotLoginException e) {
		return ErrorResponse.toResponseEntity(e.getErrorCode());
	}

	@ExceptionHandler(LoginAlreadyInUseException.class)
	public ResponseEntity<ErrorResponse> loginAlreadyInUse(LoginAlreadyInUseException e) {
		ErrorResponse errorMessage = ErrorResponse.builder()
			.detail("login is already in use, use another login")
			.build();

		return new ResponseEntity<ErrorResponse>(errorMessage, defaultHeaders, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ProjectAlreadyExistException.class)
	public ResponseEntity<ErrorResponse> projectAlreadyExist(ProjectAlreadyExistException e) {
		ErrorResponse errorMessage = ErrorResponse.builder()
			.detail("project name already exist, please use another project name")
			.build();

		return new ResponseEntity<ErrorResponse>(errorMessage, defaultHeaders, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> resourceNotFound(ResourceNotFoundException e) {
		ErrorResponse errorMessage = ErrorResponse.builder()
			.detail("resource not found")
			.build();

		return new ResponseEntity<ErrorResponse>(errorMessage, defaultHeaders, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CannotDeleteProjectOwnerException.class)
	public ResponseEntity<ErrorResponse> cannotDeleteProjectOwnerException(CannotDeleteProjectOwnerException e) {
		ErrorResponse errorMessage = ErrorResponse.builder()
			.detail("can't remove project owner")
			.build();

		return new ResponseEntity<ErrorResponse>(errorMessage, defaultHeaders, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserAlreadyInvitedException.class)
	public ResponseEntity<ErrorResponse> userAlreadyInvitedException(UserAlreadyInvitedException e) {
		ErrorResponse errorMessage = ErrorResponse.builder()
			.detail("user already invited")
			.build();

		return new ResponseEntity<ErrorResponse>(errorMessage, defaultHeaders, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UserNotInvitedException.class)
	public ResponseEntity<ErrorResponse> userNotInvitedException(UserNotInvitedException e) {
		ErrorResponse errorMessage = ErrorResponse.builder()
			.detail("you are not invited")
			.build();
		return new ResponseEntity<ErrorResponse>(errorMessage, defaultHeaders, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
		Map<String, Object> errors = toValidationError(e);
		ErrorResponse errorMessage = ErrorResponse.builder()
			.detail("validation failed")
			.data(errors)
			.build();
		return new ResponseEntity<ErrorResponse>(errorMessage, defaultHeaders, HttpStatus.BAD_REQUEST);
	}

	private Map<String, Object> toValidationError(MethodArgumentNotValidException e) {
		List<FieldError> list = e.getFieldErrors();
		Map<String, Object> result = new HashMap<>();
		for(FieldError fieldError : list) {
			ValidationError error = ValidationError.builder()
				.fieldName(fieldError.getField())
				.rejectedValue(fieldError.getRejectedValue())
				.message(fieldError.getDefaultMessage())
				.build();
			result.put(fieldError.getField(), error);
		}
		return result;
	}
}
