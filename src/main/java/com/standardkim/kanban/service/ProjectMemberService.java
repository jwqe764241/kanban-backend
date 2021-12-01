package com.standardkim.kanban.service;

import java.util.List;

import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.ProjectMember;
import com.standardkim.kanban.entity.ProjectMemberKey;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.project.CannotDeleteProjectOwnerException;
import com.standardkim.kanban.exception.project.ProjectMemberNotFoundException;
import com.standardkim.kanban.exception.project.ProjectNotFoundException;
import com.standardkim.kanban.exception.user.UserNotFoundException;
import com.standardkim.kanban.repository.ProjectInvitationRepository;
import com.standardkim.kanban.repository.ProjectMemberRepository;
import com.standardkim.kanban.repository.ProjectRepository;
import com.standardkim.kanban.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {
	private final ProjectMemberRepository projectMemberRepository;

	private final ProjectInvitationRepository projectInvitationRepository;

	private final ProjectRepository projectRepository;

	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public boolean isExist(Long projectId, Long userId){
		ProjectMemberKey key = ProjectMemberKey.from(projectId, userId);
		return projectMemberRepository.existsById(key);
	}

	@Transactional(readOnly = true)
	public boolean isProjectOwner(Long projectId, Long userId) {
		ProjectMember projectMember = findById(projectId, userId);
		return projectMember.isRegister();
	}

	@Transactional(readOnly = true)
	public ProjectMember findById(Long projectId, Long userId) {
		ProjectMemberKey key = ProjectMemberKey.from(projectId, userId);
		return projectMemberRepository.findById(key)
			.orElseThrow(() -> new ProjectMemberNotFoundException("project member not found"));
	}

	@Transactional(readOnly = true)
	public List<ProjectMember> findByProjectId(Long projectId) {
		List<ProjectMember> projectMembers = projectMemberRepository.findByProjectIdOrderByRegisterDateAsc(projectId);
		return projectMembers;
	}

	@Transactional(rollbackFor = Exception.class)
	public ProjectMember create(Long projectId, Long userId, boolean isRegister) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException("user not found"));
		Project project = projectRepository.findById(projectId)
			.orElseThrow(() -> new ProjectNotFoundException("project not found"));
		ProjectMember projectMember = ProjectMember.from(project, user, isRegister);
		return projectMemberRepository.save(projectMember);
	}

	@Transactional(rollbackFor = Exception.class)
	public void delete(Long projectId, Long userId) {
		ProjectMember member = null;

		try {
			member = findById(projectId, userId);
		} catch (ProjectMemberNotFoundException e) {
			return;
		}

		if(member.isRegister()) {
			throw new CannotDeleteProjectOwnerException("can't delete project owner");
		}

		projectInvitationRepository.deleteByProjectMemberId(projectId, userId);
		projectMemberRepository.delete(member);
	}
}
