package com.standardkim.kanban.domain.taskcolumn.api;

import java.util.List;

import javax.validation.Valid;

import com.standardkim.kanban.domain.kanban.dto.KanbanActionDto.CreateColumnAction;
import com.standardkim.kanban.domain.kanban.dto.KanbanActionDto.DeleteColumnAction;
import com.standardkim.kanban.domain.kanban.dto.KanbanActionDto.ReorderColumnAction;
import com.standardkim.kanban.domain.kanban.dto.KanbanActionDto.UpdateColumnAction;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnService;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;
import com.standardkim.kanban.domain.taskcolumn.dto.CreateTaskColumnParam;
import com.standardkim.kanban.domain.taskcolumn.dto.ReorderTaskColumnParam;
import com.standardkim.kanban.domain.taskcolumn.dto.TaskColumnDetail;
import com.standardkim.kanban.domain.taskcolumn.dto.UpdateTaskColumnParam;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
	private final TaskColumnService taskColumnService;

	private final SimpMessagingTemplate simpMessagingTemplate;

	private final ModelMapper modelMapper;

	@GetMapping("/projects/{projectId}/kanbans/{sequenceId}/columns")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public List<TaskColumnDetail> getColumns(@PathVariable Long projectId, @PathVariable Long sequenceId) {
		List<TaskColumn> taskColumns = taskColumnService.findByProjectIdAndSequenceId(projectId, sequenceId);
		List<TaskColumnDetail> taskColumnDetails = modelMapper.map(taskColumns, new TypeToken<List<TaskColumnDetail>>(){}.getType());
		return taskColumnDetails;
	}

	@PostMapping("/projects/{projectId}/kanbans/{sequenceId}/columns")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.CREATED)
	public TaskColumnDetail addColumn(@PathVariable Long projectId, @PathVariable Long sequenceId, 
		@Valid @RequestBody CreateTaskColumnParam param) {
		TaskColumn taskColumn = taskColumnService.create(projectId, sequenceId, param);
		TaskColumnDetail taskColumnDetail = modelMapper.map(taskColumn, TaskColumnDetail.class);
		simpMessagingTemplate.convertAndSend("/topic/project/" + projectId + "/kanban/" + sequenceId,
			CreateColumnAction.from(taskColumnDetail));
		return taskColumnDetail;
	}

	@PostMapping("/projects/{projectId}/kanbans/{sequenceId}/columns/reorder")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public List<TaskColumnDetail> reorderColumn(@PathVariable Long projectId, @PathVariable Long sequenceId,
		@Valid @RequestBody ReorderTaskColumnParam param) {
		List<TaskColumn> updatedTaskColumns = taskColumnService.reorder(projectId, sequenceId, param);
		List<TaskColumnDetail> updatedTaskColumnDetails = modelMapper.map(updatedTaskColumns, new TypeToken<List<TaskColumnDetail>>(){}.getType());
		simpMessagingTemplate.convertAndSend("/topic/project/" + projectId + "/kanban/" + sequenceId,
			ReorderColumnAction.from(updatedTaskColumnDetails));
		return updatedTaskColumnDetails;
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
		simpMessagingTemplate.convertAndSend("/topic/project/" + projectId + "/kanban/" + sequenceId,
			DeleteColumnAction.from(columnId, updatedTaskColumnDetail));
		return updatedTaskColumnDetail;
	}

	@PatchMapping("/projects/{projectId}/kanbans/{sequenceId}/columns/{columnId}")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public TaskColumnDetail updateColumn(@PathVariable Long projectId, @PathVariable Long sequenceId, @PathVariable Long columnId,
		@Valid @RequestBody UpdateTaskColumnParam param) {
		TaskColumn taskColumn = taskColumnService.update(projectId, sequenceId, columnId, param);
		TaskColumnDetail taskColumnDetail = modelMapper.map(taskColumn, TaskColumnDetail.class);
		simpMessagingTemplate.convertAndSend("/topic/project/" + projectId + "/kanban/" + sequenceId,
			UpdateColumnAction.from(taskColumnDetail));
		return taskColumnDetail;
	}
}
