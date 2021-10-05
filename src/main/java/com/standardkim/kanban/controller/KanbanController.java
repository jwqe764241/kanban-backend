package com.standardkim.kanban.controller;

import java.util.List;

import javax.validation.Valid;

import com.standardkim.kanban.dto.KanbanDto.CreateKanbanDTO;
import com.standardkim.kanban.dto.KanbanDto.KanbanInfoDto;
import com.standardkim.kanban.service.KanbanService;

import org.springframework.http.HttpStatus;
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
}
