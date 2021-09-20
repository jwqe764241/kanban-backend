package com.standardkim.kanban.service;

import java.util.List;
import java.util.Optional;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.dto.ProjectMemberDto.ProjectMemberInfo;
import com.standardkim.kanban.dto.UserDto.SuggestionUserInfo;
import com.standardkim.kanban.entity.ProjectMember;
import com.standardkim.kanban.entity.ProjectMemberKey;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.CannotDeleteProjectOwnerException;
import com.standardkim.kanban.exception.PermissionException;
import com.standardkim.kanban.exception.ResourceNotFoundException;
import com.standardkim.kanban.repository.ProjectMemberRepository;
import com.standardkim.kanban.repository.UserRepository;
import com.standardkim.kanban.util.AuthenticationFacade;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {
	private final ProjectMemberRepository projectMemberRepository;

	private final UserRepository userRepository;

	private final AuthenticationFacade authenticationFacade;

	private final ModelMapper modelMapper;

	@Transactional(readOnly = true)
	public boolean isMemberExists(Long projectId, Long userId){
		ProjectMemberKey id = ProjectMemberKey.builder()
			.projectId(projectId)
			.userId(userId)
			.build();
		return projectMemberRepository.existsById(id);
	}

	@Transactional(readOnly = true)
	public boolean isProjectOwner(Long projectId, Long userId) {
		try {
			ProjectMember projectMember = getProjectMemberById(projectId, userId);
			return projectMember.isRegister();
		}
		catch (ResourceNotFoundException e) {
			return false;
		}
	}

	@Transactional(readOnly = true)
	public ProjectMember getProjectMemberById(Long projectId, Long userId) {
		ProjectMemberKey key = ProjectMemberKey.builder()
			.projectId(projectId)
			.userId(userId)
			.build();
		Optional<ProjectMember> projectMember = projectMemberRepository.findById(key);
		return projectMember.orElseThrow(() -> new ResourceNotFoundException("project member not found"));
	}

	@Transactional(readOnly = true)
	public List<ProjectMemberInfo> getProjectMembersById(Long projectId) {
		SecurityUser securityUser = authenticationFacade.getSecurityUser();
		if(!isMemberExists(projectId, securityUser.getId())) {
			throw new PermissionException("you have no permission to access this project.");
		}
		List<ProjectMember> members = projectMemberRepository.findByProjectIdOrderByRegisterDateAsc(projectId);
		List<ProjectMemberInfo> result = modelMapper.map(members, new TypeToken<List<ProjectMemberInfo>>(){}.getType());
		return result;
	}

	@Transactional(readOnly = true)
	public List<SuggestionUserInfo> getUserSuggestions(Long projectId, String query) {
		List<User> users = userRepository.findUserSuggestions(projectId, query);
		List<SuggestionUserInfo> result = modelMapper.map(users, new TypeToken<List<SuggestionUserInfo>>(){}.getType());
		return result;
	}

	@Transactional(rollbackFor = Exception.class)
	public ProjectMember addProjectMemeber(Long projectId, Long userId, boolean isRegister) {
		ProjectMember projectMember = ProjectMember.builder()
			.id(new ProjectMemberKey(projectId, userId))
			.isRegister(isRegister)
			.build();
		projectMember = projectMemberRepository.save(projectMember);
		return projectMember;
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteProjectMember(Long projectId, Long userId) {
		SecurityUser securityUser = authenticationFacade.getSecurityUser();
		if(!isProjectOwner(projectId, securityUser.getId())) {
			throw new PermissionException("no permission to access project [" + projectId + "]");
		}

		ProjectMember member = null;
		try {
			member = getProjectMemberById(projectId, userId);
		} catch (ResourceNotFoundException e) {
			return;
		}

		if(member.isRegister()) {
			throw new CannotDeleteProjectOwnerException();
		}

		projectMemberRepository.delete(member);
	}
}
