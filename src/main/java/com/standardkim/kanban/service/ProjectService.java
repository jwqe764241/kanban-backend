package com.standardkim.kanban.service;

import java.util.ArrayList;
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
	public Project getProjectById(Long projectId) {
		Optional<Project> project = projectRepository.findById(projectId);
		return project.orElseThrow(() -> new ResourceNotFoundException("resource not found"));
	}	

	@Transactional(readOnly = true)
	public List<ProjectDetail> getMyProjectDetails() {
		User user = userService.findBySecurityUser();
		Set<ProjectMember> projectMembers = user.getProjects();
		ArrayList<ProjectDetail> projectDetails = modelMapper.map(projectMembers, new TypeToken<List<ProjectDetail>>(){}.getType());
		return projectDetails;
	}

	@Transactional(readOnly = true)
	public ProjectDetail getProjectDetailById(Long projectId) {
		Project project = getProjectById(projectId);
		ProjectDetail detail = modelMapper.map(project, ProjectDetail.class);
		return detail;
	}

	@Transactional(rollbackFor = Exception.class) 
	public Project createProject(CreateProjectParam createProjectParam, User registerUser) {
		Project project = Project.builder()
			.name(createProjectParam.getName())
			.description(createProjectParam.getDescription())
			.registerUser(registerUser)
			.build();
		project = projectRepository.save(project);
		return project;
	}

	@Transactional(rollbackFor = Exception.class)
	public Project createProject(CreateProjectParam createProjectParam) {
		if(isProjectNameExists(createProjectParam.getName())) {
			throw new ProjectAlreadyExistException("project already exist.");
		}
		User user = userService.findBySecurityUser();
		Project project = createProject(createProjectParam, user);
		projectMemberService.addProjectMemeber(project.getId(), user.getId(), true);
		return project;
	}
}
