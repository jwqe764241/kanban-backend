package com.standardkim.kanban.domain.kanban.application;

import com.standardkim.kanban.domain.kanban.dao.KanbanRepository;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.kanban.dto.CreateKanbanParam;
import com.standardkim.kanban.domain.kanban.dto.UpdateKanbanParam;
import com.standardkim.kanban.domain.kanban.exception.KanbanNotFoundException;
import com.standardkim.kanban.domain.project.application.ProjectService;
import com.standardkim.kanban.domain.project.domain.Project;

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