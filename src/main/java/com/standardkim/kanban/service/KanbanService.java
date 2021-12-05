package com.standardkim.kanban.service;

import java.util.List;

import com.standardkim.kanban.dto.KanbanDto.CreateKanbanParam;
import com.standardkim.kanban.dto.KanbanDto.UpdateKanbanParam;
import com.standardkim.kanban.entity.Kanban;
import com.standardkim.kanban.entity.KanbanSequence;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.exception.kanban.KanbanNotFoundException;
import com.standardkim.kanban.repository.KanbanRepository;
import com.standardkim.kanban.repository.KanbanSequenceRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KanbanService {
	private final KanbanRepository kanbanRepository;

	private final KanbanSequenceRepository kanbanSequenceRepository;

	private final ProjectService projectService;

	@Transactional(readOnly = true)
	public List<KanbanSequence> findByProjectId(Long projectId) {
		return kanbanSequenceRepository.findByProjectIdAndIsDeletedOrderBySequenceId(projectId, false);
	}

	@Transactional(readOnly = true)
	public KanbanSequence findByProjectIdAndSequenceId(Long projectId, Long sequenceId) {
		return kanbanSequenceRepository.findByProjectIdAndSequenceId(projectId, sequenceId)
			.orElseThrow(() -> new KanbanNotFoundException("kanban not found"));
	}

	@Transactional(readOnly = true)
	public KanbanSequence findById(Long kanbanId) {
		return kanbanSequenceRepository.findById(kanbanId)
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
		Kanban kanban = kanbanRepository.findByProjectIdAndSequenceId(projectId, sequenceId)
			.orElseThrow(() -> new KanbanNotFoundException("kanban not found"));
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