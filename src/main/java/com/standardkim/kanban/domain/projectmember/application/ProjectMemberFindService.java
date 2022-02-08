package com.standardkim.kanban.domain.projectmember.application;

import java.util.List;

import com.standardkim.kanban.domain.projectmember.dao.ProjectMemberRepository;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMember;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMemberKey;
import com.standardkim.kanban.domain.projectmember.exception.ProjectMemberNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectMemberFindService {
	private final ProjectMemberRepository projectMemberRepository;

	@Transactional(readOnly = true)
	public boolean isExist(Long projectId, Long userId){
		ProjectMemberKey key = ProjectMemberKey.of(projectId, userId);
		return projectMemberRepository.existsById(key);
	}

	@Transactional(readOnly = true)
	public ProjectMember findById(Long projectId, Long userId) {
		ProjectMemberKey key = ProjectMemberKey.of(projectId, userId);
		return projectMemberRepository.findById(key)
			.orElseThrow(() -> new ProjectMemberNotFoundException("project member not found"));
	}

	@Transactional(readOnly = true)
	public List<ProjectMember> findByProjectId(Long projectId) {
		return projectMemberRepository.findByProjectIdOrderByCreatedAtAsc(projectId);
	}
}
