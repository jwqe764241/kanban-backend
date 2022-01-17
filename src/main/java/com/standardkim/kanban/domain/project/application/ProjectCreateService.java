package com.standardkim.kanban.domain.project.application;

import com.standardkim.kanban.domain.project.dao.ProjectRepository;
import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.project.dto.CreateProjectParam;
import com.standardkim.kanban.domain.project.exception.DuplicateProjectNameException;
import com.standardkim.kanban.domain.user.application.UserService;
import com.standardkim.kanban.domain.user.domain.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectCreateService {
	private final ProjectFindService projectFindService;

	private final UserService userService;

	private final ProjectRepository projectRepository;

	@Transactional(rollbackFor = Exception.class)
	public Project create(Long userId, CreateProjectParam createProjectParam) {
		if(projectFindService.isNameExist(createProjectParam.getName())) {
			throw new DuplicateProjectNameException("duplicate project name");
		}
		User user = userService.findById(userId);
		Project project = Project.of(createProjectParam.getName(), createProjectParam.getDescription(), user);
		Project newProject = projectRepository.save(project);
		newProject.addMemberAsRegister(user);
		return newProject;
	}
}
