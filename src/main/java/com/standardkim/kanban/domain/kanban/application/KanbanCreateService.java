package com.standardkim.kanban.domain.kanban.application;

import com.standardkim.kanban.domain.kanban.dao.KanbanRepository;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.kanban.dto.CreateKanbanParam;
import com.standardkim.kanban.domain.project.application.ProjectFindService;
import com.standardkim.kanban.domain.project.domain.Project;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KanbanCreateService {
	private final ProjectFindService projectFindService;

	private final KanbanRepository kanbanRepository;

	@Transactional(rollbackFor = Exception.class)
	public Kanban create(Long projectId, CreateKanbanParam createKanbanParam) {
		Project project = projectFindService.findById(projectId);
		Kanban kanban = Kanban.of(createKanbanParam.getName(), createKanbanParam.getDescription(), project);
		return kanbanRepository.save(kanban);
	}
}
