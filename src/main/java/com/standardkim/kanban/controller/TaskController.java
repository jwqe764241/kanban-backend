package com.standardkim.kanban.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.standardkim.kanban.dto.KanbanActionDto.CreateTaskAction;
import com.standardkim.kanban.dto.KanbanActionDto.DeleteTaskAction;
import com.standardkim.kanban.dto.KanbanActionDto.ReorderTaskAction;
import com.standardkim.kanban.dto.TaskDto.CreateTaskParam;
import com.standardkim.kanban.dto.TaskDto.ReorderTaskParam;
import com.standardkim.kanban.dto.TaskDto.TaskDetail;
import com.standardkim.kanban.entity.Task;
import com.standardkim.kanban.service.TaskService;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
public class TaskController {
	private final TaskService taskService;

	private final SimpMessagingTemplate simpMessagingTemplate;

	private final ModelMapper modelMapper;

	@GetMapping("/projects/{projectId}/kanbans/{sequenceId}/columns/tasks")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public Map<Long, List<TaskDetail>> getTasks(@PathVariable Long projectId, @PathVariable Long sequenceId) {
		Map<Long, List<TaskDetail>> tasks = taskService.findTasksByKanbanSequence(projectId, sequenceId);
		return tasks;
	}

	@PostMapping("/projects/{projectId}/kanbans/{sequenceId}/columns/{columnId}/tasks")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.CREATED)
	public List<TaskDetail> createTask(@PathVariable Long projectId, @PathVariable Long sequenceId, @PathVariable Long columnId,
		@Valid @RequestBody CreateTaskParam param) {
		List<Task> task = taskService.create(projectId, sequenceId, columnId, param);
		List<TaskDetail> taskDetail = modelMapper.map(task, new TypeToken<List<TaskDetail>>(){}.getType());
		simpMessagingTemplate.convertAndSend("/topic/project/" + projectId + "/kanban/" + sequenceId, 
			CreateTaskAction.from(taskDetail));
		return taskDetail;
	}

	@DeleteMapping("/projects/{projectId}/kanbans/{sequenceId}/columns/{columnId}/tasks/{taskId}")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public TaskDetail deleteTask(@PathVariable Long projectId, @PathVariable Long sequenceId, 
		@PathVariable Long columnId, @PathVariable Long taskId) {
		Task updatedTask = taskService.delete(projectId, sequenceId, columnId, taskId);
		TaskDetail updatedTaskDetail = null;
		if(updatedTask != null) {
			updatedTaskDetail = modelMapper.map(updatedTask, TaskDetail.class);
		}
		simpMessagingTemplate.convertAndSend("/topic/project/" + projectId + "/kanban/" + sequenceId, 
			DeleteTaskAction.from(columnId, taskId, updatedTaskDetail));
		return updatedTaskDetail;
	}

	@PostMapping("/projects/{projectId}/kanbans/{sequenceId}/columns/{columnId}/reorder")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public List<TaskDetail> reorderTask(@PathVariable Long projectId, @PathVariable Long sequenceId, 
		@PathVariable Long columnId, @RequestBody ReorderTaskParam param) {
		List<Task> updatedTasks = taskService.reorder(projectId, sequenceId, columnId, param);
		List<TaskDetail> updatedTaskDetails = modelMapper.map(updatedTasks, new TypeToken<List<TaskDetail>>(){}.getType());
		simpMessagingTemplate.convertAndSend("/topic/project/" + projectId + "/kanban/" + sequenceId, 
			ReorderTaskAction.from(updatedTaskDetails));
		return updatedTaskDetails;
	}
}
