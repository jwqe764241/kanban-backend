package com.standardkim.kanban.domain.projectmember.application;

import com.standardkim.kanban.domain.projectinvitation.application.ProjectInvitationDeleteService;
import com.standardkim.kanban.domain.projectmember.dao.ProjectMemberRepository;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMember;
import com.standardkim.kanban.domain.projectmember.exception.CannotDeleteAdminMemberException;
import com.standardkim.kanban.domain.projectmember.exception.ProjectMemberNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectMemberDeleteService {
	private final ProjectMemberFindService projectMemberFindService;

	private final ProjectInvitationDeleteService projectInvitationDeleteService;

	private final ProjectMemberRepository projectMemberRepository;

	private final ProjectRoleHierarchy projectRoleHierarchy;

	@Transactional(rollbackFor = Exception.class)
	public void delete(Long projectId, Long userId) {
		ProjectMember member = null;

		try {
			member = projectMemberFindService.findById(projectId, userId);
		} catch (ProjectMemberNotFoundException e) {
			return;
		}

		if(projectRoleHierarchy.hasAdminRole(member.getProjectRole().getName())) {
			throw new CannotDeleteAdminMemberException("can't delete admin member");
		}

		projectInvitationDeleteService.deleteByProjectIdAndUserId(projectId, userId);
		projectMemberRepository.delete(member);
	}

	@Transactional
	public void deleteByProjectId(Long projectId) {
		projectInvitationDeleteService.deleteByProjectId(projectId);
		projectMemberRepository.deleteByProjectId(projectId);
	}
}
