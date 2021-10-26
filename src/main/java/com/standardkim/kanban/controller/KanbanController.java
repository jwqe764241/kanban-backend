package com.standardkim.kanban.controller;

import java.util.List;

import javax.validation.Valid;

import com.standardkim.kanban.dto.KanbanDto.CreateKanbanParam;
import com.standardkim.kanban.dto.KanbanDto.KanbanDetail;
import com.standardkim.kanban.dto.KanbanDto.UpdateKanbanParam;
import com.standardkim.kanban.entity.Kanban;
import com.standardkim.kanban.service.KanbanService;

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
public class KanbanController {
	private final KanbanService kanbanService;

	@PostMapping("/projects/{projectId}/kanbans")
	@PreAuthorize("isProjectOwner(#projectId)")
	@ResponseStatus(HttpStatus.CREATED)
	public KanbanDetail createKanban(@PathVariable Long projectId, @RequestBody @Valid CreateKanbanParam createKanbanParam) {
		Kanban kanban = kanbanService.create(projectId, createKanbanParam);
		return kanbanService.findKanbanDetailById(kanban.getId());
	}

	@GetMapping("/projects/{projectId}/kanbans")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public List<KanbanDetail> getKanbans(@PathVariable Long projectId) {
		return kanbanService.findKanbanDetailByProjectId(projectId);
	}

	@GetMapping("/projects/{projectId}/kanbans/{sequenceId}")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public KanbanDetail getKanban(@PathVariable Long projectId, @PathVariable Long sequenceId) {
		return kanbanService.findKanbanDetailByProjectIdAndSequenceId(projectId, sequenceId);
	}

	@PatchMapping("/projects/{projectId}/kanbans/{sequenceId}")
	@PreAuthorize("isProjectOwner(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public void updateKanban(@PathVariable Long projectId, @PathVariable Long sequenceId, @RequestBody @Valid UpdateKanbanParam updateKanbanParam) {
		kanbanService.update(projectId, sequenceId, updateKanbanParam);
	}

	@DeleteMapping("/projects/{projectId}/kanbans/{sequenceId}")
	@PreAuthorize("isProjectOwner(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public void removeKanban(@PathVariable Long projectId, @PathVariable Long sequenceId) {
		kanbanService.delete(projectId, sequenceId);
	}
}
