package com.standardkim.kanban.domain.project.application;

import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.project.dto.UpdateProjectParam;
import com.standardkim.kanban.domain.project.exception.DuplicateProjectNameException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectUpdateService {
	private final ProjectFindService projectFindService;

	@Transactional(rollbackFor = Exception.class)
	public Project update(Long projectId, UpdateProjectParam updateProjectParam) {
		if(projectFindService.isNameExist(updateProjectParam.getName())) {
			throw new DuplicateProjectNameException("duplicate project name");
		}
		Project project = projectFindService.findById(projectId);
		project.updateName(updateProjectParam.getName());
		return project;
	}
}
