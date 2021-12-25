package com.standardkim.kanban.service;

import java.util.List;

import com.standardkim.kanban.dto.ProjectDto.CreateProjectParam;
import com.standardkim.kanban.dto.ProjectDto.UpdateProjectParam;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.project.DuplicateProjectNameException;
import com.standardkim.kanban.exception.project.ProjectNotFoundException;
import com.standardkim.kanban.repository.ProjectRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
	private final ProjectRepository projectRepository;

	private final UserService userService;

	@Transactional(readOnly = true)
	public boolean isProjectNameExist(String name) {
		return projectRepository.existsByName(name);
	}

	@Transactional(readOnly = true)
	public Project findById(Long id) {
		return projectRepository.findById(id)
			.orElseThrow(() -> new ProjectNotFoundException("resource not found"));
	}	

	@Transactional(readOnly = true)
	public List<Project> findByUserId(Long id) {
		return projectRepository.findByUserId(id);
	}

	@Transactional(rollbackFor = Exception.class)
	public Project create(Long userId, CreateProjectParam createProjectParam) {
		if(isProjectNameExist(createProjectParam.getName())) {
			throw new DuplicateProjectNameException("duplicate project name");
		}
		User registerUser = userService.findById(userId);
		Project project = Project.from(createProjectParam, registerUser);
		Project createdProject = projectRepository.save(project);
		createdProject.addMember(registerUser, true);
		return createdProject;
	}

	@Transactional(rollbackFor = Exception.class)
	public Project update(Long projectId, UpdateProjectParam updateProjectParam) {
		if(isProjectNameExist(updateProjectParam.getName())) {
			throw new DuplicateProjectNameException("duplicate project name");
		}
		Project project = findById(projectId);
		project.update(updateProjectParam);
		return project;
	}
}
