package com.standardkim.kanban.domain.kanban.application;

import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.kanban.exception.KanbanNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KanbanDeleteService {
	private final KanbanFindService kanbanFindService;

	@Transactional(rollbackFor = Exception.class)
	public void delete(Long projectId, Long sequenceId) {
		Kanban kanban = null;

		try {
			kanban = kanbanFindService.findByProjectIdAndSequenceId(projectId, sequenceId);
		} catch (KanbanNotFoundException e) {
			return;
		}

		if(!kanban.isDeleted()) {
			kanban.delete();
		}
	}
}
