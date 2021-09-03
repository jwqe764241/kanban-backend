package com.standardkim.kanban.service;

import java.util.ArrayList;
import java.util.List;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.dto.ProjectMemberDto.ProjectMemberInfo;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.ProjectMember;
import com.standardkim.kanban.entity.ProjectMemberKey;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.CannotDeleteProjectOwnerException;
import com.standardkim.kanban.exception.PermissionException;
import com.standardkim.kanban.exception.ResourceNotFoundException;
import com.standardkim.kanban.repository.ProjectMemberRepository;
import com.standardkim.kanban.repository.ProjectRepository;
import com.standardkim.kanban.util.AuthenticationFacade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {
	private final ProjectMemberRepository projectMemberRepository;

	private final ProjectRepository projectRepository;

	private final ProjectService projectService;

	private final AuthenticationFacade authenticationFacade;

	public boolean isMemberExists(Long projectId, Long userId){
		ProjectMemberKey id = ProjectMemberKey.builder()
			.projectId(projectId)
			.userId(userId)
			.build();
		return projectMemberRepository.existsById(id);
	}

	@Transactional(rollbackFor = Exception.class, readOnly = true)
	public List<ProjectMemberInfo> getProjectMembersById(Long projectId) {
		Project project = projectRepository.findById(projectId)
			.orElseThrow(() -> new ResourceNotFoundException("project not found"));

		SecurityUser securityUser = authenticationFacade.getSecurityUser();
		if(!isMemberExists(project.getId(), securityUser.getId())) {
			throw new PermissionException("you have no permission to access this project.");
		}

		List<ProjectMember> members = projectMemberRepository.findByProjectIdOrderByRegisterDateAsc(project.getId());
		List<ProjectMemberInfo> result = new ArrayList<>(members.size());
		for(ProjectMember member : members) {
			User user = member.getUser();
			ProjectMemberInfo info = ProjectMemberInfo.builder()
				.id(user.getId())
				.name(user.getName())
				.email(user.getEmail())
				.date(member.getRegisterDate())
				.build();
			result.add(info);
		}
		
		return result;
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteProjectMember(Long projectId, Long userId) {
		SecurityUser securityUser = authenticationFacade.getSecurityUser();
		if(!projectService.isProjectOwner(projectId, securityUser.getId())) {
			throw new PermissionException("no permission to access project [" + projectId + "]");
		}

		ProjectMemberKey key = ProjectMemberKey.builder()
			.projectId(projectId)
			.userId(userId)
			.build();
		ProjectMember member = projectMemberRepository.findById(key).orElseGet(() -> null);
		if(member == null) {
			return;
		}

		if(member.isRegister()) {
			throw new CannotDeleteProjectOwnerException();
		}

		projectMemberRepository.delete(member);
	}
}
