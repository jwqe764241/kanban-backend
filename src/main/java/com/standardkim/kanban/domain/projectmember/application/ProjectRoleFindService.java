package com.standardkim.kanban.domain.projectmember.application;

import com.standardkim.kanban.domain.projectmember.dao.ProjectRoleRepository;
import com.standardkim.kanban.domain.projectmember.domain.ProjectRole;
import com.standardkim.kanban.domain.projectmember.dto.ProjectRoleName;
import com.standardkim.kanban.domain.projectmember.exception.ProjectRoleNotFoundException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectRoleFindService {
	private final ProjectRoleRepository projectRoleRepository;

	public ProjectRole findByName(ProjectRoleName name) {
		return projectRoleRepository.findByName(name)
			.orElseThrow(() -> new ProjectRoleNotFoundException("project role not found!"));
	}
}
