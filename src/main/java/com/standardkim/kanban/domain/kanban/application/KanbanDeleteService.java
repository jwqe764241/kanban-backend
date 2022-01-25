package com.standardkim.kanban.domain.kanban.application;

import com.standardkim.kanban.domain.kanban.dao.KanbanRepository;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.kanban.exception.KanbanNotFoundException;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnDeleteService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KanbanDeleteService {
	private final KanbanFindService kanbanFindService;

	private final TaskColumnDeleteService taskColumnDeleteService;

	private final KanbanRepository kanbanRepository;

	@Transactional(rollbackFor = Exception.class)
	public void delete(Long projectId, Long sequenceId) {
		Kanban kanban = null;

		try {
			kanban = kanbanFindService.findByProjectIdAndSequenceId(projectId, sequenceId);
		} catch (KanbanNotFoundException e) {
			return;
		}

		taskColumnDeleteService.deleteByKanbanId(kanban.getId());
		kanbanRepository.delete(kanban);
	}
}
