package com.standardkim.kanban.domain.projectinvitation.application;

import java.util.List;

import com.standardkim.kanban.domain.projectinvitation.dao.ProjectInvitationRepository;
import com.standardkim.kanban.domain.projectinvitation.domain.ProjectInvitation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectInvitationFindService {
	private final ProjectInvitationRepository projectInvitationRepository;

	@Transactional(readOnly = true)
	public boolean isExist(Long projectId, Long invitedUserId) {
		return projectInvitationRepository.existsByProjectIdAndInvitedUserId(projectId, invitedUserId);
	}

	@Transactional(readOnly = true)
	public List<ProjectInvitation> findByProjectId(Long projectId) {
		List<ProjectInvitation> invitations = projectInvitationRepository.findByProjectId(projectId);
		return invitations;
	}
}
