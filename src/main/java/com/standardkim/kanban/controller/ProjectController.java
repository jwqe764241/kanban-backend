package com.standardkim.kanban.controller;

import java.util.ArrayList;

import javax.validation.Valid;

import com.standardkim.kanban.dto.ProjectDto.NewProjectRequest;
import com.standardkim.kanban.dto.ProjectDto.ProjectInfo;
import com.standardkim.kanban.service.ProjectService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProjectController {
	private final ProjectService projectService;

	@PostMapping("/projects")
	@ResponseStatus(HttpStatus.CREATED)
	public void createProject(@RequestBody @Valid NewProjectRequest newProjectRequest) {
		projectService.createProject(newProjectRequest.getName(), newProjectRequest.getDescription());
	}

	@GetMapping("/projects")
	@ResponseStatus(HttpStatus.OK)
	public ArrayList<ProjectInfo> getMyProject() {
		return projectService.getMyProjects();
	}

	@GetMapping("/projects/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ProjectInfo getProject(@PathVariable Long id, Authentication authentication) {
		return projectService.getProjectById(id);
	}
}
