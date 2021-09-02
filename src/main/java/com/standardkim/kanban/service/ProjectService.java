package com.standardkim.kanban.service;

import java.util.ArrayList;
import java.util.Set;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.dto.ProjectDto.ProjectInfo;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.ProjectMember;
import com.standardkim.kanban.entity.ProjectMemberKey;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.PermissionException;
import com.standardkim.kanban.exception.ProjectAlreadyExistException;
import com.standardkim.kanban.exception.ResourceNotFoundException;
import com.standardkim.kanban.exception.UserNotFoundException;
import com.standardkim.kanban.repository.ProjectMemberRepository;
import com.standardkim.kanban.repository.ProjectRepository;
import com.standardkim.kanban.repository.UserRepository;
import com.standardkim.kanban.util.AuthenticationFacade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
	private final ProjectRepository projectRepository;

	private final UserRepository userRepository;

	private final ProjectMemberRepository projectMemberRepository;

	private final AuthenticationFacade authenticationFacade;

	@Transactional(rollbackFor = Exception.class)
	public Project createProject(String name, String description) {
		if(projectRepository.existsByName(name)) {
			throw new ProjectAlreadyExistException("project already exist.");
		}

		SecurityUser securityUser = authenticationFacade.getSecurityUser();

		User user = userRepository.findById(securityUser.getId())
			.orElseThrow(() -> new UserNotFoundException("use not found"));

		Project newProject = Project.builder()
			.name(name)
			.description(description)
			.registerUser(user)
			.build();
		newProject = projectRepository.save(newProject);

		//Add to project member as register
		ProjectMember projectMember = ProjectMember.builder()
			.id(new ProjectMemberKey(newProject.getId(), user.getId()))
			.isRegister(true)
			.build();
		projectMemberRepository.save(projectMember);
		
		return newProject;
	}

	@Transactional(rollbackFor = Exception.class, readOnly = true)
	public ArrayList<ProjectInfo> getMyProjects() {
		SecurityUser securityUser = authenticationFacade.getSecurityUser();

		User user = userRepository.findById(securityUser.getId())
			.orElseThrow(() -> new UserNotFoundException("user not found"));

		Set<ProjectMember> projectMembers = user.getProjects();
		ArrayList<ProjectInfo> projects = new ArrayList<ProjectInfo>(projectMembers.size());

		for(ProjectMember member : projectMembers) {
			Project project = member.getProject();
			ProjectInfo info = ProjectInfo.builder()
				.id(project.getId())
				.name(project.getName())
				.description(project.getDescription())
				.registerDate(project.getRegisterDate())
				.registerUsername(project.getRegisterUser().getName())
				.build();
			projects.add(info);
		}

		return projects;
	}

	@Transactional(rollbackFor = Exception.class, readOnly = true)
	public ProjectInfo getProjectById(Long projectId) {
		SecurityUser user = authenticationFacade.getSecurityUser();
	
		Project project = projectRepository.findById(projectId)
			.orElseThrow(() -> new ResourceNotFoundException("resource not found"));

		ProjectMemberKey key = ProjectMemberKey.builder()
			.userId(user.getId())
			.projectId(projectId)
			.build();
		if(!projectMemberRepository.existsById(key)) {
			throw new PermissionException("no permission to access project [" + projectId + "]");
		}

		ProjectInfo info = ProjectInfo.builder()
			.id(project.getId())
			.name(project.getName())
			.description(project.getDescription())
			.registerDate(project.getRegisterDate())
			.registerUsername(project.getRegisterUser().getName())
			.build();

		return info;
	}
}
