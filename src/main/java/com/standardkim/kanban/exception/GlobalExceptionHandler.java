package com.standardkim.kanban.exception;

import javax.annotation.PostConstruct;

import com.standardkim.kanban.dto.ErrorMessageDto.ErrorMessage;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@ExceptionHandler(LoginAlreadyInUse.class)
	public ResponseEntity<ErrorMessage> loginAlreadyInUse(LoginAlreadyInUse e) {
		ErrorMessage errorMessage = ErrorMessage.builder()
			.message("join error")
			.detail("login is already in use, use another login")
			.build();

		return new ResponseEntity<ErrorMessage>(errorMessage, defaultHeaders, HttpStatus.CONFLICT);
	}
}
