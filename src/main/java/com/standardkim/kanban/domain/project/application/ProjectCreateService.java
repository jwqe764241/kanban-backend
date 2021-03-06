package com.standardkim.kanban.domain.project.application;

import com.standardkim.kanban.domain.project.dao.ProjectRepository;
import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.project.dto.CreateProjectParam;
import com.standardkim.kanban.domain.project.exception.DuplicateProjectNameException;
import com.standardkim.kanban.domain.projectmember.application.ProjectRoleFindService;
import com.standardkim.kanban.domain.projectmember.domain.ProjectRole;
import com.standardkim.kanban.domain.projectmember.dto.ProjectRoleName;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.domain.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectCreateService {
	private final ProjectFindService projectFindService;

	private final UserFindService userFindService;

	private final ProjectRepository projectRepository;

	private final ProjectRoleFindService projectRoleFindService;

	@Transactional(rollbackFor = Exception.class)
	public Project create(Long userId, CreateProjectParam createProjectParam) {
		if(projectFindService.isNameExist(createProjectParam.getName())) {
			throw new DuplicateProjectNameException("duplicate project name");
		}
		User user = userFindService.findById(userId);
		Project project = Project.of(createProjectParam.getName(), createProjectParam.getDescription(), user);
		Project newProject = projectRepository.save(project);
		ProjectRole adminRole = projectRoleFindService.findByName(ProjectRoleName.ADMIN);
		newProject.addMember(user, adminRole);
		return newProject;
	}
}
