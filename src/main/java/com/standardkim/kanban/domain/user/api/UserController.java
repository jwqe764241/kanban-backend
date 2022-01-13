package com.standardkim.kanban.domain.user.api;

import javax.validation.Valid;

import com.standardkim.kanban.domain.user.application.UserService;
import com.standardkim.kanban.domain.user.dto.UserDto.CreateUserParam;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@PostMapping("/users")
	@ResponseStatus(HttpStatus.CREATED)
	public void join(@RequestBody @Valid CreateUserParam createUserParam) {
		userService.create(createUserParam);
	}
}
