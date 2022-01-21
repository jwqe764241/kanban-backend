package com.standardkim.kanban.domain.task.api;

import java.util.List;

import javax.validation.Valid;

import com.standardkim.kanban.domain.task.application.TaskActionService;
import com.standardkim.kanban.domain.task.application.TaskCreateService;
import com.standardkim.kanban.domain.task.application.TaskDeleteService;
import com.standardkim.kanban.domain.task.application.TaskFindService;
import com.standardkim.kanban.domain.task.application.TaskReorderService;
import com.standardkim.kanban.domain.task.application.TaskUpdateService;
import com.standardkim.kanban.domain.task.domain.Task;
import com.standardkim.kanban.domain.task.dto.CreateTaskParam;
import com.standardkim.kanban.domain.task.dto.ReorderTaskParam;
import com.standardkim.kanban.domain.task.dto.TaskDetail;
import com.standardkim.kanban.domain.task.dto.UpdateTaskParam;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TaskApi {
	private final TaskFindService taskFindService;

	private final TaskCreateService taskCreateService;

	private final TaskUpdateService taskUpdateService;
	
	private final TaskDeleteService taskDeleteService;
	
	private final TaskReorderService taskReorderService;

	private final TaskActionService taskActionService;

	private final ModelMapper modelMapper;

	@GetMapping("/projects/{projectId}/kanbans/{sequenceId}/columns/tasks")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public List<TaskDetail> getTasks(@PathVariable Long projectId, @PathVariable Long sequenceId) {
		List<Task> tasks = taskFindService.findByProjectIdAndSequenceId(projectId, sequenceId);
		List<TaskDetail> taskDetails = modelMapper.map(tasks, new TypeToken<List<TaskDetail>>(){}.getType());
		return taskDetails;
	}

	@PostMapping("/projects/{projectId}/kanbans/{sequenceId}/columns/{columnId}/tasks")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.CREATED)
	public List<TaskDetail> createTask(@PathVariable Long projectId, @PathVariable Long sequenceId, @PathVariable Long columnId,
		@Valid @RequestBody CreateTaskParam param) {
		List<Task> updatedTasks = taskCreateService.create(projectId, sequenceId, columnId, param);
		List<TaskDetail> updatedTaskDetails = modelMapper.map(updatedTasks, new TypeToken<List<TaskDetail>>(){}.getType());
		taskActionService.sendCreateAction(projectId, sequenceId, updatedTaskDetails);
		return updatedTaskDetails;
	}

	@PatchMapping("/projects/{projectId}/kanbans/{sequenceId}/columns/{columnId}/tasks/{taskId}")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public TaskDetail updateTask(@PathVariable Long projectId, @PathVariable Long sequenceId, 
	@PathVariable Long columnId, @PathVariable Long taskId, @Valid @RequestBody UpdateTaskParam param) {
		Task updatedTask = taskUpdateService.update(projectId, sequenceId, columnId, taskId, param);
		TaskDetail updatedTaskDetail = modelMapper.map(updatedTask, TaskDetail.class);
		taskActionService.sendUpdateAction(projectId, sequenceId, updatedTaskDetail);
		return updatedTaskDetail;
	}

	@DeleteMapping("/projects/{projectId}/kanbans/{sequenceId}/columns/{columnId}/tasks/{taskId}")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public TaskDetail deleteTask(@PathVariable Long projectId, @PathVariable Long sequenceId, 
		@PathVariable Long columnId, @PathVariable Long taskId) {
		Task updatedTask = taskDeleteService.delete(projectId, sequenceId, columnId, taskId);
		TaskDetail updatedTaskDetail = null;
		if(updatedTask != null) {
			updatedTaskDetail = modelMapper.map(updatedTask, TaskDetail.class);
		}
		taskActionService.sendDeleteAction(projectId, sequenceId, taskId, updatedTaskDetail);
		return updatedTaskDetail;
	}

	@PostMapping("/projects/{projectId}/kanbans/{sequenceId}/columns/{columnId}/reorder")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public List<TaskDetail> reorderTask(@PathVariable Long projectId, @PathVariable Long sequenceId, 
		@PathVariable Long columnId, @RequestBody ReorderTaskParam param) {
		List<Task> updatedTasks = taskReorderService.reorder(projectId, sequenceId, columnId, param);
		List<TaskDetail> updatedTaskDetails = modelMapper.map(updatedTasks, new TypeToken<List<TaskDetail>>(){}.getType());
		taskActionService.sendReorderAction(projectId, sequenceId, updatedTaskDetails);
		return updatedTaskDetails;
	}
}
