package com.standardkim.kanban.domain.projectmember.application;

import com.standardkim.kanban.domain.project.application.ProjectFindService;
import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.projectinvitation.application.ProjectInvitationDeleteService;
import com.standardkim.kanban.domain.projectinvitation.application.ProjectInvitationFindService;
import com.standardkim.kanban.domain.projectmember.dao.ProjectMemberRepository;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMember;
import com.standardkim.kanban.domain.projectmember.domain.ProjectRole;
import com.standardkim.kanban.domain.projectmember.dto.ProjectRoleName;
import com.standardkim.kanban.domain.projectmember.exception.InvitationNotFoundException;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.domain.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectMemberAcceptService {
	private final ProjectInvitationFindService projectInvitationFindService;

	private final ProjectInvitationDeleteService projectInvitationDeleteService;

	private final ProjectFindService projectFindService;

	private final UserFindService userFindService;

	private final ProjectMemberRepository projectMemberRepository;
	
	private final ProjectRoleFindService projectRoleFindService;

	@Transactional(rollbackFor = Exception.class)
	public ProjectMember accept(Long projectId, Long userId) {
		if(!projectInvitationFindService.isExist(projectId, userId)) {
			throw new InvitationNotFoundException("user not invited");
		}
		Project project = projectFindService.findById(projectId);
		User user = userFindService.findById(userId);
		ProjectRole memberRole = projectRoleFindService.findByName(ProjectRoleName.MEMBER);
		ProjectMember projectMember = ProjectMember.of(project, user, memberRole);
		ProjectMember newProjectMember = projectMemberRepository.save(projectMember);
		projectInvitationDeleteService.deleteByProjectIdAndInvitedUserId(projectId, userId);
		return newProjectMember;
	}
}
