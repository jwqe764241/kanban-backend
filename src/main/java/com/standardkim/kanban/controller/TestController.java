package com.standardkim.kanban.controller;

import javax.validation.Valid;

import com.standardkim.kanban.dto.UserDto;
import com.standardkim.kanban.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {

	private final UserService userService;

	@PostMapping("/join")
	@ResponseStatus(HttpStatus.CREATED)
	public void join(@RequestBody @Valid UserDto.JoinUserRequest joinUserRequest) {
		userService.join(joinUserRequest);
	}
}
