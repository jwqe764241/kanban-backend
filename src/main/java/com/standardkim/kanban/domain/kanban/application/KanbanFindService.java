package com.standardkim.kanban.domain.kanban.application;

import com.standardkim.kanban.domain.kanban.dao.KanbanRepository;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.kanban.exception.KanbanNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KanbanFindService {
	private final KanbanRepository kanbanRepository;

	@Transactional(readOnly = true)
	public Kanban findByProjectIdAndSequenceId(Long projectId, Long sequenceId) {
		return kanbanRepository.findByProjectIdAndSequenceId(projectId, sequenceId)
			.orElseThrow(() -> new KanbanNotFoundException("kanban not found"));
	}
}
