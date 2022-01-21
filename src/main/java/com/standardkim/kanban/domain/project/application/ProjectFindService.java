package com.standardkim.kanban.domain.project.application;

import java.util.List;

import com.standardkim.kanban.domain.project.dao.ProjectRepository;
import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.project.exception.ProjectNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectFindService {
	private final ProjectRepository projectRepository;

	@Transactional(readOnly = true)
	public boolean isNameExist(String name) {
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
}
