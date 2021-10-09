package com.standardkim.kanban.service;

import java.util.List;
import java.util.Optional;

import com.standardkim.kanban.entity.KanbanSequence;
import com.standardkim.kanban.exception.ResourceNotFoundException;
import com.standardkim.kanban.repository.KanbanSequenceRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KanbanSequenceService {
	private final KanbanSequenceRepository kanbanSequenceRepository;
	
	@Transactional(readOnly = true)
	public KanbanSequence findById(Long id) {
		final Optional<KanbanSequence> kanbanSequence = kanbanSequenceRepository.findById(id);
		return kanbanSequence.orElseThrow(() -> new ResourceNotFoundException("resource not found"));
	}

	@Transactional(readOnly = true)
	public KanbanSequence findByProjectIdAndSequenceId(Long projectId, Long sequenceId) {
		final Optional<KanbanSequence> kanbanSequence = kanbanSequenceRepository.findByProjectIdAndSequenceId(projectId, sequenceId);
		return kanbanSequence.orElseThrow(() -> new ResourceNotFoundException("resource not found"));
	}

	@Transactional(readOnly = true)
	public List<KanbanSequence> findByProjectIdAndNotDeleted(Long projectId) {
		final Optional<List<KanbanSequence>> kanbanSequences = kanbanSequenceRepository.findByProjectIdAndIsDeletedOrderBySequenceId(projectId, false);
		return kanbanSequences.orElseThrow(() -> new ResourceNotFoundException("resource not found"));
	}
}
