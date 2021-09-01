package com.standardkim.kanban.controller;

import javax.validation.Valid;

import com.standardkim.kanban.dto.UserDto.JoinUserRequest;
import com.standardkim.kanban.dto.UserDto.NewUserInfo;
import com.standardkim.kanban.service.UserService;

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
	public void join(@RequestBody @Valid JoinUserRequest joinUserRequest) {
		NewUserInfo newUserInfo = NewUserInfo.from(joinUserRequest);
		userService.addUser(newUserInfo);
	}
}
