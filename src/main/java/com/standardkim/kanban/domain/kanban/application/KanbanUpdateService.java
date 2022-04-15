package com.standardkim.kanban.domain.kanban.application;

import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.kanban.dto.UpdateKanbanDescriptionParam;
import com.standardkim.kanban.domain.kanban.dto.UpdateKanbanNameParam;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KanbanUpdateService {
	private final KanbanFindService kanbanFindService;

	@Transactional(rollbackFor = Exception.class)
	public Kanban updateName(Long projectId, Long sequenceId, UpdateKanbanNameParam param) {
		Kanban kanban = kanbanFindService.findByProjectIdAndSequenceId(projectId, sequenceId);
		kanban.updateName(param.getName());
		return kanban;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Kanban updateDescription(Long projectId, Long sequenceId, UpdateKanbanDescriptionParam param) {
		Kanban kanban = kanbanFindService.findByProjectIdAndSequenceId(projectId, sequenceId);
		kanban.updateDescription(param.getDescription());
		return kanban;
	}
}
