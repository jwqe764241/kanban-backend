package com.standardkim.kanban.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
public class TestController {
	@GetMapping("/welcome")
	public String welcome() {
		return "Shit";
	}
}
