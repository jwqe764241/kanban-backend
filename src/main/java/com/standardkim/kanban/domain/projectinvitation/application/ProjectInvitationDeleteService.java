package com.standardkim.kanban.domain.projectinvitation.application;

import com.standardkim.kanban.domain.projectinvitation.dao.ProjectInvitationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectInvitationDeleteService {
	private final ProjectInvitationRepository projectInvitationRepository;

	@Transactional(rollbackFor = Exception.class)
	public void deleteByProjectIdAndInvitedUserId(Long projectId, Long invitedUserId) {
		projectInvitationRepository.deleteByProjectIdAndInvitedUserId(projectId, invitedUserId);
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteByProjectIdAndUserId(Long projectId, Long userId) {
		projectInvitationRepository.deleteByProjectIdAndUserId(projectId, userId);
	}

	@Transactional
	public void deleteByProjectId(Long projectId) {
		projectInvitationRepository.deleteByProjectId(projectId);
	}
}
