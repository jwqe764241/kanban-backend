package com.standardkim.kanban.controller;

import java.util.List;

import javax.validation.Valid;

import com.standardkim.kanban.dto.KanbanActionDto.CreateColumnAction;
import com.standardkim.kanban.dto.KanbanActionDto.DeleteColumnAction;
import com.standardkim.kanban.dto.TaskColumnDto.CreateTaskColumnParam;
import com.standardkim.kanban.dto.TaskColumnDto.TaskColumnDetail;
import com.standardkim.kanban.entity.TaskColumn;
import com.standardkim.kanban.service.TaskColumnService;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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

	private final ModelMapper modelMapper;

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
	public TaskColumnDetail addColumn(@PathVariable Long projectId, @PathVariable Long sequenceId, 
		@Valid @RequestBody CreateTaskColumnParam param) {
		TaskColumn taskColumn = taskColumnService.create(projectId, sequenceId, param);
		TaskColumnDetail taskColumnDetail = modelMapper.map(taskColumn, TaskColumnDetail.class);
		simpMessagingTemplate.convertAndSend("/topic/kanban/" + sequenceId,
			CreateColumnAction.from(taskColumnDetail));
		return taskColumnDetail;
	}

	@DeleteMapping("/projects/{projectId}/kanbans/{sequenceId}/columns/{columnId}")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public TaskColumnDetail deleteColumn(@PathVariable Long projectId, @PathVariable Long sequenceId, @PathVariable Long columnId) {
		TaskColumn updatedTaskColumn = taskColumnService.delete(columnId);
		TaskColumnDetail updatedTaskColumnDetail = null;
		if(updatedTaskColumn != null) {
			updatedTaskColumnDetail = modelMapper.map(updatedTaskColumn, TaskColumnDetail.class);
		}
		simpMessagingTemplate.convertAndSend("/topic/kanban/" + sequenceId,
			DeleteColumnAction.from(columnId, updatedTaskColumnDetail));
		return updatedTaskColumnDetail;
	}
}
