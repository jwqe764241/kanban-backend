package com.standardkim.kanban.controller;

import java.util.List;

import javax.validation.Valid;

import com.standardkim.kanban.dto.KanbanDto.CreateKanbanDTO;
import com.standardkim.kanban.dto.KanbanDto.KanbanInfoDto;
import com.standardkim.kanban.dto.KanbanDto.UpdateKanbanDto;
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
	public KanbanInfoDto createKanban(@PathVariable Long projectId, @RequestBody @Valid CreateKanbanDTO createKanbanDto) {
		return kanbanService.createKanban(projectId, createKanbanDto);
	}

	@GetMapping("/projects/{projectId}/kanbans")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public List<KanbanInfoDto> getKanbans(@PathVariable Long projectId) {
		return kanbanService.getKanbanInfosByProjectId(projectId);
	}

	@GetMapping("/projects/{projectId}/kanbans/{sequenceId}")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public KanbanInfoDto getKanban(@PathVariable Long projectId, @PathVariable Long sequenceId) {
		return kanbanService.getKanbanInfoBySequenceId(projectId, sequenceId);
	}

	@PatchMapping("/projects/{projectId}/kanbans/{sequenceId}")
	@PreAuthorize("isProjectOwner(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public void updateKanbanInfo(@PathVariable Long projectId, @PathVariable Long sequenceId, @RequestBody @Valid UpdateKanbanDto updateKanbanDto) {
		kanbanService.updateKanbanInfo(projectId, sequenceId, updateKanbanDto);
	}

	@DeleteMapping("/projects/{projectId}/kanbans/{sequenceId}")
	@PreAuthorize("isProjectOwner(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public void removeKanban(@PathVariable Long projectId, @PathVariable Long sequenceId) {
		kanbanService.deleteKanban(projectId, sequenceId);
	}
}