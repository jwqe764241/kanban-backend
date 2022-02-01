package com.standardkim.kanban.domain.project.application;

import com.standardkim.kanban.domain.kanban.application.KanbanDeleteService;
import com.standardkim.kanban.domain.project.dao.ProjectRepository;
import com.standardkim.kanban.domain.projectmember.application.ProjectMemberDeleteService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectDeleteService {
	private final KanbanDeleteService kanbanDeleteService;

	private final ProjectMemberDeleteService projectMemberDeleteService;

	private final ProjectRepository projectRepository;

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void delete(Long projectId) {
		projectMemberDeleteService.deleteByProjectId(projectId); // delete invitation and member
		kanbanDeleteService.deleteByProjectId(projectId); // delete task, task column and kanban
		projectRepository.deleteById(projectId);
	}
}
