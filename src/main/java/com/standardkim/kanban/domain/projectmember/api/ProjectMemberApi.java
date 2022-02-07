package com.standardkim.kanban.domain.projectmember.api;

import java.util.List;

import com.standardkim.kanban.domain.projectmember.application.ProjectMemberAcceptService;
import com.standardkim.kanban.domain.projectmember.application.ProjectMemberDeleteService;
import com.standardkim.kanban.domain.projectmember.application.ProjectMemberFindService;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMember;
import com.standardkim.kanban.domain.projectmember.dto.ProjectMemberDetail;
import com.standardkim.kanban.global.auth.dto.SecurityUser;
import com.standardkim.kanban.global.util.SecurityContextFacade;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProjectMemberApi {
	private final ProjectMemberFindService projectMemberFindService;

	private final ProjectMemberAcceptService projectMemberAcceptService;

	private final ProjectMemberDeleteService projectMemberDeleteService;

	private final ModelMapper modelMapper;

	@GetMapping("/projects/{projectId}/members")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasProjectRole(#projectId, 'MEMBER')")
	public List<ProjectMemberDetail> getProjectMember(@PathVariable Long projectId) {
		List<ProjectMember> projectMembers = projectMemberFindService.findByProjectId(projectId);
		List<ProjectMemberDetail> projectMemberDetails = modelMapper.map(projectMembers, new TypeToken<List<ProjectMemberDetail>>(){}.getType());
		return projectMemberDetails;
	}

	@PostMapping("/projects/{projectId}/members/accept-invitation")
	@ResponseStatus(HttpStatus.OK)
	public void acceptInvitation(@PathVariable Long projectId) {
		SecurityUser securityUser = SecurityContextFacade.getSecurityUser();
		projectMemberAcceptService.accept(projectId, securityUser.getId());
	}

	@DeleteMapping("/projects/{projectId}/members/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasProjectRole(#projectId, 'ADMIN')")
	public void removeProjectMember(@PathVariable Long projectId, @PathVariable Long userId) {
		projectMemberDeleteService.delete(projectId, userId);
	}
}
