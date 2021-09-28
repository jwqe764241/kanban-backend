package com.standardkim.kanban.exception;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import com.standardkim.kanban.dto.ErrorMessageDto.ErrorMessage;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private HttpHeaders defaultHeaders;

	@PostConstruct
	public void init() {
		defaultHeaders = new HttpHeaders();
		defaultHeaders.add("Content-Type", "application/json; charset=UTF-8");
	}

	@ExceptionHandler(ExpiredRefreshTokenException.class)
	public ResponseEntity<ErrorMessage> expiredRefreshToken(ExpiredRefreshTokenException e) {
		ErrorMessage errorMessage = ErrorMessage.builder()
			.message("invalid refresh token")
			.detail("given refresh token is expired. please login")
			.build();

		return new ResponseEntity<ErrorMessage>(errorMessage, defaultHeaders, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(RefreshTokenNotMatchedException.class)
	public ResponseEntity<ErrorMessage> refreshTokenNotMatched(RefreshTokenNotMatchedException e) {
		ErrorMessage errorMessage = ErrorMessage.builder()
			.message("invalid refresh token")
			.build();

		return new ResponseEntity<ErrorMessage>(errorMessage, defaultHeaders, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(TokenNotProvidedException.class)
	public ResponseEntity<ErrorMessage> tokenNotProvided(TokenNotProvidedException e) {
		ErrorMessage errorMessage = ErrorMessage.builder()
			.message("invalid reqeust parameter")
			.detail("token for refresh not provided, check parameter")
			.build();

		return new ResponseEntity<ErrorMessage>(errorMessage, defaultHeaders, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RefreshTokenNotFoundException.class)
	public ResponseEntity<ErrorMessage> refreshTokenNotFound(RefreshTokenNotFoundException e) {
		ErrorMessage errorMessage = ErrorMessage.builder()
			.message("token error")
			.detail("can't find user's refresh token. please login")
			.build();
		
		return new ResponseEntity<ErrorMessage>(errorMessage, defaultHeaders, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorMessage> userNotFound(UserNotFoundException e) {
		ErrorMessage errorMessage = ErrorMessage.builder()
			.message("token error")
			.detail("can't find user. check account")
			.build();

		return new ResponseEntity<ErrorMessage>(errorMessage, defaultHeaders, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(LoginFailedException.class)
	public ResponseEntity<ErrorMessage> loginFailed(LoginFailedException e) {
		ErrorMessage errorMessage = ErrorMessage.builder()
			.message("login error")
			.detail("login failed. check username of password")
			.build();

		return new ResponseEntity<ErrorMessage>(errorMessage, defaultHeaders, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(LoginAlreadyInUseException.class)
	public ResponseEntity<ErrorMessage> loginAlreadyInUse(LoginAlreadyInUseException e) {
		ErrorMessage errorMessage = ErrorMessage.builder()
			.message("join error")
			.detail("login is already in use, use another login")
			.build();

		return new ResponseEntity<ErrorMessage>(errorMessage, defaultHeaders, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ProjectAlreadyExistException.class)
	public ResponseEntity<ErrorMessage> projectAlreadyExist(ProjectAlreadyExistException e) {
		ErrorMessage errorMessage = ErrorMessage.builder()
			.message("create error")
			.detail("project name already exist, please use another project name")
			.build();

		return new ResponseEntity<ErrorMessage>(errorMessage, defaultHeaders, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorMessage> resourceNotFound(ResourceNotFoundException e) {
		ErrorMessage errorMessage = ErrorMessage.builder()
			.message("resource error")
			.detail("resource not found")
			.build();

		return new ResponseEntity<ErrorMessage>(errorMessage, defaultHeaders, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CannotDeleteProjectOwnerException.class)
	public ResponseEntity<ErrorMessage> cannotDeleteProjectOwnerException(CannotDeleteProjectOwnerException e) {
		ErrorMessage errorMessage = ErrorMessage.builder()
			.message("request error")
			.detail("can't remove project owner")
			.build();

		return new ResponseEntity<ErrorMessage>(errorMessage, defaultHeaders, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserAlreadyInvitedException.class)
	public ResponseEntity<ErrorMessage> userAlreadyInvitedException(UserAlreadyInvitedException e) {
		ErrorMessage errorMessage = ErrorMessage.builder()
			.message("invite error")
			.detail("user already invited")
			.build();

		return new ResponseEntity<ErrorMessage>(errorMessage, defaultHeaders, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UserNotInvitedException.class)
	public ResponseEntity<ErrorMessage> userNotInvitedException(UserNotInvitedException e) {
		ErrorMessage errorMessage = ErrorMessage.builder()
			.message("invite error")
			.detail("you are not invited")
			.build();
		return new ResponseEntity<ErrorMessage>(errorMessage, defaultHeaders, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException e) {
		Map<String, Object> errors = toValidationError(e);
		ErrorMessage errorMessage = ErrorMessage.builder()
			.message("validation error")
			.detail("validation failed")
			.data(errors)
			.build();
		return new ResponseEntity<ErrorMessage>(errorMessage, defaultHeaders, HttpStatus.BAD_REQUEST);
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
