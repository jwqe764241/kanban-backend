package com.standardkim.kanban.domain.kanban.api;

import java.util.List;

import javax.validation.Valid;

import com.standardkim.kanban.domain.kanban.application.KanbanSequenceService;
import com.standardkim.kanban.domain.kanban.application.KanbanService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.kanban.domain.KanbanSequence;
import com.standardkim.kanban.domain.kanban.dto.CreateKanbanParam;
import com.standardkim.kanban.domain.kanban.dto.KanbanDetail;
import com.standardkim.kanban.domain.kanban.dto.UpdateKanbanParam;

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
public class KanbanApi {
	private final KanbanService kanbanService;

	private final KanbanSequenceService kanbanSequenceService;

	private final ModelMapper modelMapper;

	@PostMapping("/projects/{projectId}/kanbans")
	@PreAuthorize("isProjectOwner(#projectId)")
	@ResponseStatus(HttpStatus.CREATED)
	public KanbanDetail createKanban(@PathVariable Long projectId, @RequestBody @Valid CreateKanbanParam createKanbanParam) {
		Kanban kanban = kanbanService.create(projectId, createKanbanParam);
		KanbanSequence kanbanSequence = kanbanSequenceService.findById(kanban.getId());
		KanbanDetail kanbanDetail = modelMapper.map(kanbanSequence, KanbanDetail.class);
		return kanbanDetail;
	}

	@GetMapping("/projects/{projectId}/kanbans")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public List<KanbanDetail> getKanbans(@PathVariable Long projectId) {
		List<KanbanSequence> kanbanSequences = kanbanSequenceService.findByProjectId(projectId);
		List<KanbanDetail> kanbanDetails = modelMapper.map(kanbanSequences, new TypeToken<List<KanbanDetail>>(){}.getType());
		return kanbanDetails;
	}

	@GetMapping("/projects/{projectId}/kanbans/{sequenceId}")
	@PreAuthorize("isProjectMember(#projectId)")
	@ResponseStatus(HttpStatus.OK)
	public KanbanDetail getKanban(@PathVariable Long projectId, @PathVariable Long sequenceId) {
		KanbanSequence kanbanSequence = kanbanSequenceService.findByProjectIdAndSequenceId(projectId, sequenceId);
		KanbanDetail kanbanDetail = modelMapper.map(kanbanSequence, KanbanDetail.class);
		return kanbanDetail;
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
