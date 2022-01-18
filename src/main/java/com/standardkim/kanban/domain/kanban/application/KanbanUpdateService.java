package com.standardkim.kanban.domain.kanban.application;

import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.kanban.dto.UpdateKanbanParam;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KanbanUpdateService {
	private final KanbanFindService kanbanFindService;

	@Transactional(rollbackFor = Exception.class)
	public Kanban update(Long projectId, Long sequenceId, UpdateKanbanParam updateKanbanParam) {
		Kanban kanban = kanbanFindService.findByProjectIdAndSequenceId(projectId, sequenceId);
		kanban.updateName(updateKanbanParam.getName());
		kanban.updateDescription(updateKanbanParam.getDescription());
		return kanban;
	}
}
