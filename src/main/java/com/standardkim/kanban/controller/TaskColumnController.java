package com.standardkim.kanban.controller;

import java.util.List;

import javax.validation.Valid;

import com.standardkim.kanban.dto.TaskColumnDto.CreateTaskColumnParam;
import com.standardkim.kanban.dto.TaskColumnDto.TaskColumnDetail;
import com.standardkim.kanban.service.TaskColumnService;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TaskColumnController {
	private final TaskColumnService taskColumnService;

	private final SimpMessagingTemplate simpMessagingTemplate;

	@GetMapping("/projects/{projectId}/kanbans/{sequenceId}/columns")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public List<TaskColumnDetail> getColumns(@PathVariable Long projectId, @PathVariable Long sequenceId) {
		List<TaskColumnDetail> details = taskColumnService.findTaskColumnDetails(projectId, sequenceId);
		return details;
	}

	@PostMapping("/projects/{projectId}/kanbans/{sequenceId}/columns")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.CREATED)
	public void addColumn(@PathVariable Long projectId, @PathVariable Long sequenceId, 
		@Valid @RequestBody CreateTaskColumnParam param) {
		taskColumnService.create(projectId, sequenceId, param);
		simpMessagingTemplate.convertAndSend("/topic/kanban/" + sequenceId, "aa");
	}
}
