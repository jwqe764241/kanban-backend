package com.standardkim.kanban.controller;

import javax.validation.Valid;

import com.standardkim.kanban.dto.ProjectDto.NewProjectRequest;
import com.standardkim.kanban.service.ProjectService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProjectController {
	private final ProjectService projectService;

	@PostMapping("/project")
	@ResponseStatus(HttpStatus.CREATED)
	public void createProject(@RequestBody @Valid NewProjectRequest newProjectRequest) {
		projectService.createProject(newProjectRequest.getName(), newProjectRequest.getDescription());
	}
}
