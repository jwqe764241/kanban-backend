package com.standardkim.kanban.controller;

import java.util.List;
import java.util.Map;

import com.standardkim.kanban.dto.TaskDto.TaskDetail;
import com.standardkim.kanban.service.TaskService;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TaskController {
	private final TaskService taskService;

	@GetMapping("/projects/{projectId}/kanbans/{sequenceId}/columns/tasks")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public Map<Long, List<TaskDetail>> getTasks(@PathVariable Long projectId, @PathVariable Long sequenceId) {
		Map<Long, List<TaskDetail>> tasks = taskService.findTasksByKanbanSequence(projectId, sequenceId);
		return tasks;
	}
}
