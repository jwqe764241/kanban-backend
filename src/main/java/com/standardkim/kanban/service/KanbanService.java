package com.standardkim.kanban.service;

import com.standardkim.kanban.dto.KanbanDto.CreateKanbanParam;
import com.standardkim.kanban.dto.KanbanDto.UpdateKanbanParam;
import com.standardkim.kanban.entity.Kanban;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.exception.kanban.KanbanNotFoundException;
import com.standardkim.kanban.repository.KanbanRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KanbanService {
	private final KanbanRepository kanbanRepository;

	private final ProjectService projectService;

	@Transactional(readOnly = true)
	public Kanban findByProjectIdAndSequenceId(Long projectId, Long sequenceId) {
		return kanbanRepository.findByProjectIdAndSequenceId(projectId, sequenceId)
			.orElseThrow(() -> new KanbanNotFoundException("kanban not found"));
	}

	@Transactional(rollbackFor = Exception.class)
	public Kanban create(Long projectId, CreateKanbanParam createKanbanParam) {
		Project project = projectService.findById(projectId);
		Kanban kanban = Kanban.from(createKanbanParam, project);
		return kanbanRepository.save(kanban);
	}

	@Transactional(rollbackFor = Exception.class)
	public Kanban update(Long projectId, Long sequenceId, UpdateKanbanParam updateKanbanParam) {
		Kanban kanban = findByProjectIdAndSequenceId(projectId, sequenceId);
		kanban.updateKanban(updateKanbanParam);
		return kanban;
	}

	@Transactional(rollbackFor = Exception.class)
	public void delete(Long projectId, Long sequenceId) {
		Kanban kanban = kanbanRepository.findByProjectIdAndSequenceId(projectId, sequenceId).orElse(null);
		if(kanban != null && !kanban.isDeleted()) {
			kanban.updateToDeleted();
		}
	}
}