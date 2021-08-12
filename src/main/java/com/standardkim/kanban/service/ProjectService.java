package com.standardkim.kanban.service;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.ProjectAlreadyExistException;
import com.standardkim.kanban.exception.UserNotFoundException;
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
			.user(user)
			.build();

		newProject = projectRepository.save(newProject);
		
		return newProject;
	}
}
