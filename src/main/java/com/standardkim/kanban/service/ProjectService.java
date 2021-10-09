package com.standardkim.kanban.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.standardkim.kanban.dto.ProjectDto.CreateProjectParam;
import com.standardkim.kanban.dto.ProjectDto.ProjectDetail;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.ProjectMember;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.ProjectAlreadyExistException;
import com.standardkim.kanban.exception.ResourceNotFoundException;
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
		Optional<Project> project = projectRepository.findById(id);
		return project.orElseThrow(() -> new ResourceNotFoundException("resource not found"));
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
		Project project = Project.builder()
			.name(createProjectParam.getName())
			.description(createProjectParam.getDescription())
			.registerUser(registerUser)
			.build();
		project = projectRepository.save(project);
		return project;
	}

	@Transactional(rollbackFor = Exception.class)
	public Project create(CreateProjectParam createProjectParam) {
		if(isProjectNameExists(createProjectParam.getName())) {
			throw new ProjectAlreadyExistException("project already exist.");
		}
		User user = userService.findBySecurityUser();
		Project project = create(createProjectParam, user);
		projectMemberService.addProjectMemeber(project.getId(), user.getId(), true);
		return project;
	}
}
