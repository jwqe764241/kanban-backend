package com.standardkim.kanban.domain.taskcolumn.api;

import java.util.List;

import javax.validation.Valid;

import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnActionService;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnCreateService;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnDeleteService;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnFindService;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnReorderService;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnUpdateService;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;
import com.standardkim.kanban.domain.taskcolumn.dto.CreateTaskColumnParam;
import com.standardkim.kanban.domain.taskcolumn.dto.ReorderTaskColumnParam;
import com.standardkim.kanban.domain.taskcolumn.dto.TaskColumnDetail;
import com.standardkim.kanban.domain.taskcolumn.dto.UpdateTaskColumnParam;

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
public class TaskColumnApi {
	private final TaskColumnFindService taskColumnFindService;

	private final TaskColumnCreateService taskColumnCreateService;

	private final TaskColumnUpdateService taskColumnUpdateService;
	
	private final TaskColumnDeleteService taskColumnDeleteService;

	private final TaskColumnReorderService taskColumnReorderService;

	private final TaskColumnActionService taskColumnActionService;

	private final ModelMapper modelMapper;

	@GetMapping("/projects/{projectId}/kanbans/{sequenceId}/columns")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public List<TaskColumnDetail> getTaskColumns(@PathVariable Long projectId, @PathVariable Long sequenceId) {
		List<TaskColumn> taskColumns = taskColumnFindService.findByProjectIdAndSequenceId(projectId, sequenceId);
		List<TaskColumnDetail> taskColumnDetails = modelMapper.map(taskColumns, new TypeToken<List<TaskColumnDetail>>(){}.getType());
		return taskColumnDetails;
	}

	@PostMapping("/projects/{projectId}/kanbans/{sequenceId}/columns")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.CREATED)
	public TaskColumnDetail createTaskColumn(@PathVariable Long projectId, @PathVariable Long sequenceId, 
		@Valid @RequestBody CreateTaskColumnParam param) {
		TaskColumn taskColumn = taskColumnCreateService.create(projectId, sequenceId, param);
		TaskColumnDetail taskColumnDetail = modelMapper.map(taskColumn, TaskColumnDetail.class);
		taskColumnActionService.sendCreateAction(projectId, sequenceId, taskColumnDetail);
		return taskColumnDetail;
	}

	@PatchMapping("/projects/{projectId}/kanbans/{sequenceId}/columns/{columnId}")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public TaskColumnDetail updateTaskColumn(@PathVariable Long projectId, @PathVariable Long sequenceId, @PathVariable Long columnId,
		@Valid @RequestBody UpdateTaskColumnParam param) {
		TaskColumn taskColumn = taskColumnUpdateService.update(projectId, sequenceId, columnId, param);
		TaskColumnDetail taskColumnDetail = modelMapper.map(taskColumn, TaskColumnDetail.class);
		taskColumnActionService.sendUpdateAction(projectId, sequenceId, taskColumnDetail);
		return taskColumnDetail;
	}

	@DeleteMapping("/projects/{projectId}/kanbans/{sequenceId}/columns/{columnId}")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public TaskColumnDetail deleteTaskColumn(@PathVariable Long projectId, @PathVariable Long sequenceId, @PathVariable Long columnId) {
		TaskColumn updatedTaskColumn = taskColumnDeleteService.delete(columnId);
		TaskColumnDetail updatedTaskColumnDetail = null;
		if(updatedTaskColumn != null) {
			updatedTaskColumnDetail = modelMapper.map(updatedTaskColumn, TaskColumnDetail.class);
		}
		taskColumnActionService.sendDeleteAction(projectId, sequenceId, columnId, updatedTaskColumnDetail);
		return updatedTaskColumnDetail;
	}

	@PostMapping("/projects/{projectId}/kanbans/{sequenceId}/columns/reorder")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public List<TaskColumnDetail> reorderTaskColumn(@PathVariable Long projectId, @PathVariable Long sequenceId,
		@Valid @RequestBody ReorderTaskColumnParam param) {
		List<TaskColumn> updatedTaskColumns = taskColumnReorderService.reorder(projectId, sequenceId, param);
		List<TaskColumnDetail> updatedTaskColumnDetails = modelMapper.map(updatedTaskColumns, new TypeToken<List<TaskColumnDetail>>(){}.getType());
		taskColumnActionService.sendReorderAction(projectId, sequenceId, updatedTaskColumnDetails);
		return updatedTaskColumnDetails;
	}
}
