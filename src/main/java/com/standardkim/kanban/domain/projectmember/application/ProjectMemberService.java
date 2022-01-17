package com.standardkim.kanban.domain.projectmember.application;

import java.util.List;

import com.standardkim.kanban.domain.project.application.ProjectService;
import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.projectinvitation.application.ProjectInvitationService;
import com.standardkim.kanban.domain.projectmember.dao.ProjectMemberRepository;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMember;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMemberKey;
import com.standardkim.kanban.domain.projectmember.exception.CannotDeleteProjectOwnerException;
import com.standardkim.kanban.domain.projectmember.exception.InvitationNotFoundException;
import com.standardkim.kanban.domain.projectmember.exception.ProjectMemberNotFoundException;
import com.standardkim.kanban.domain.user.application.UserService;
import com.standardkim.kanban.domain.user.domain.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {
	private final ProjectMemberRepository projectMemberRepository;

	private final ProjectInvitationService projectInvitationService;

	private final ProjectService projectService;

	private final UserService userService;

	@Transactional(readOnly = true)
	public boolean isExist(Long projectId, Long userId){
		ProjectMemberKey key = ProjectMemberKey.of(projectId, userId);
		return projectMemberRepository.existsById(key);
	}

	@Transactional(readOnly = true)
	public boolean isProjectOwner(Long projectId, Long userId) {
		ProjectMember projectMember = findById(projectId, userId);
		return projectMember.isRegister();
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

	@Transactional(rollbackFor = Exception.class)
	public ProjectMember create(Long projectId, Long userId, boolean isRegister) {
		Project project = projectService.findById(projectId);
		User user = userService.findById(userId);
		ProjectMember projectMember = ProjectMember.of(project, user, isRegister);
		return projectMemberRepository.save(projectMember);
	}

	@Transactional(rollbackFor = Exception.class)
	public void accept(Long projectId, Long userId) {
		if(!projectInvitationService.isExist(projectId, userId)) {
			throw new InvitationNotFoundException("user not invited");
		}
		create(projectId, userId, false);
		projectInvitationService.deleteByProjectIdAndInvitedUserId(projectId, userId);
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

		projectInvitationService.deleteByProjectIdAndUserId(projectId, userId);
		projectMemberRepository.delete(member);
	}
}
