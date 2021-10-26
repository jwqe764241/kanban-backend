package com.standardkim.kanban.service;

import java.util.List;
import java.util.Set;

import com.standardkim.kanban.dto.ProjectDto.CreateProjectParam;
import com.standardkim.kanban.dto.ProjectDto.ProjectDetail;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.ProjectMember;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.project.DuplicateProjectNameException;
import com.standardkim.kanban.exception.project.ProjectNotFoundException;
import com.standardkim.kanban.repository.ProjectRepository;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
	private final ProjectRepository projectRepository;

	private final ProjectMemberService projectMemberService;

	private final UserService userService;

	private final ModelMapper modelMapper;

	@Transactional(readOnly = true)
	public boolean isProjectNameExists(String name) {
		return projectRepository.existsByName(name);
	}

	@Transactional(readOnly = true)
	public Project findById(Long id) {
		return projectRepository.findById(id)
			.orElseThrow(() -> new ProjectNotFoundException("resource not found"));
	}	

	@Transactional(readOnly = true)
	public List<Project> findByUserId(Long id) {
		User user = userService.findById(id);
		Set<ProjectMember> projectMembers = user.getProjects();
		List<Project> projects = modelMapper.map(projectMembers, new TypeToken<List<Project>>(){}.getType());
		return projects;
	}

	@Transactional(readOnly = true)
	public List<Project> findBySecurityUser() {
		User user = userService.findBySecurityUser();
		return findByUserId(user.getId());
	}

	@Transactional(readOnly = true)
	public List<ProjectDetail> findProjectDetailBySecurityUser() {
		List<Project> projects = findBySecurityUser();
		List<ProjectDetail> projectDetails = modelMapper.map(projects, new TypeToken<List<ProjectDetail>>(){}.getType());
		return projectDetails;
	}

	@Transactional(readOnly = true)
	public ProjectDetail findProjectDetailById(Long id) {
		Project project = findById(id);
		ProjectDetail detail = modelMapper.map(project, ProjectDetail.class);
		return detail;
	}

	@Transactional(rollbackFor = Exception.class) 
	public Project create(CreateProjectParam createProjectParam, User registerUser) {
		Project project = createProjectParam.toEntity(registerUser);
		return projectRepository.save(project);
	}

	@Transactional(rollbackFor = Exception.class)
	public Project create(CreateProjectParam createProjectParam) {
		if(isProjectNameExists(createProjectParam.getName())) {
			throw new DuplicateProjectNameException("duplicate project name");
		}
		User user = userService.findBySecurityUser();
		Project project = create(createProjectParam, user);
		projectMemberService.create(project.getId(), user.getId(), true);
		return project;
	}
}
