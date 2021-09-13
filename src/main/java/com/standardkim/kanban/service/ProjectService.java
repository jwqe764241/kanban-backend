package com.standardkim.kanban.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.dto.ProjectDto.ProjectInfo;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.ProjectMember;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.PermissionException;
import com.standardkim.kanban.exception.ProjectAlreadyExistException;
import com.standardkim.kanban.exception.ResourceNotFoundException;
import com.standardkim.kanban.repository.ProjectRepository;
import com.standardkim.kanban.util.AuthenticationFacade;

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

	private final AuthenticationFacade authenticationFacade;

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
	public ArrayList<ProjectInfo> getMyProjects() {
		User user = userService.getAuthenticatedUser();
		Set<ProjectMember> projectMembers = user.getProjects();
		ArrayList<ProjectInfo> projects = modelMapper.map(projectMembers, new TypeToken<List<ProjectInfo>>(){}.getType());
		return projects;
	}

	@Transactional(readOnly = true)
	public ProjectInfo getProjectInfoById(Long projectId) {
		SecurityUser user = authenticationFacade.getSecurityUser();
		Project project = getProjectById(projectId);
		if(!projectMemberService.isMemberExists(project.getId(), user.getId())) {
			throw new PermissionException("no permission to access project [" + projectId + "]");
		}
		ProjectInfo info = modelMapper.map(project, ProjectInfo.class);
		return info;
	}

	@Transactional(rollbackFor = Exception.class) 
	public Project createProject(String name, String description, User registerUser) {
		Project project = Project.builder()
			.name(name)
			.description(description)
			.registerUser(registerUser)
			.build();
		project = projectRepository.save(project);
		return project;
	}

	@Transactional(rollbackFor = Exception.class)
	public Project createProject(String name, String description) {
		if(isProjectNameExists(name)) {
			throw new ProjectAlreadyExistException("project already exist.");
		}
		User user = userService.getAuthenticatedUser();
		Project project = createProject(name, description, user);
		projectMemberService.addProjectMemeber(project.getId(), user.getId(), true);
		return project;
	}
}
