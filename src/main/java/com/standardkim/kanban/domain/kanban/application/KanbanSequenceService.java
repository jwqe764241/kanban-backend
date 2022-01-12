package com.standardkim.kanban.domain.kanban.application;

import java.util.List;

import com.standardkim.kanban.domain.kanban.domain.KanbanSequence;
import com.standardkim.kanban.domain.kanban.domain.KanbanSequenceRepository;
import com.standardkim.kanban.domain.kanban.exception.KanbanNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KanbanSequenceService {
	private final KanbanSequenceRepository kanbanSequenceRepository;

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
}
