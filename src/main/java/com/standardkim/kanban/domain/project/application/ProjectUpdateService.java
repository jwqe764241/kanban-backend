package com.standardkim.kanban.domain.project.application;

import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.project.dto.UpdateProjectDescriptionParam;
import com.standardkim.kanban.domain.project.dto.UpdateProjectNameParam;
import com.standardkim.kanban.domain.project.exception.DuplicateProjectNameException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectUpdateService {
	private final ProjectFindService projectFindService;

	@Transactional(rollbackFor = Exception.class)
	public Project updateName(Long projectId, UpdateProjectNameParam param) {
		if(projectFindService.isNameExist(param.getName())) {
			throw new DuplicateProjectNameException("duplicate project name");
		}
		Project project = projectFindService.findById(projectId);
		project.updateName(param.getName());
		return project;
	}

	@Transactional(rollbackFor = Exception.class)
	public Project updateDescription(Long projectId, UpdateProjectDescriptionParam param) {
		Project project = projectFindService.findById(projectId);
		project.updateDescription(param.getDescription());
		return project;
	}
}
