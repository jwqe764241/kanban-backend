package com.standardkim.kanban.service;

import java.util.List;
import java.util.Optional;

import com.standardkim.kanban.dto.ProjectMemberDto.ProjectMemberDetail;
import com.standardkim.kanban.dto.UserDto.SuggestionUserDetail;
import com.standardkim.kanban.entity.ProjectMember;
import com.standardkim.kanban.entity.ProjectMemberKey;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.CannotDeleteProjectOwnerException;
import com.standardkim.kanban.exception.ResourceNotFoundException;
import com.standardkim.kanban.repository.ProjectMemberRepository;
import com.standardkim.kanban.repository.UserRepository;

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

	private final ModelMapper modelMapper;

	@Transactional(readOnly = true)
	public boolean isExists(Long projectId, Long userId){
		ProjectMemberKey key = ProjectMemberKey.builder()
			.projectId(projectId)
			.userId(userId)
			.build();
		return projectMemberRepository.existsById(key);
	}

	@Transactional(readOnly = true)
	public boolean isProjectOwner(Long projectId, Long userId) {
		try {
			ProjectMember projectMember = findById(projectId, userId);
			return projectMember.isRegister();
		}
		catch (ResourceNotFoundException e) {
			return false;
		}
	}

	@Transactional(readOnly = true)
	public ProjectMember findById(ProjectMemberKey projectMemberKey) {
		Optional<ProjectMember> projectMember = projectMemberRepository.findById(projectMemberKey);
		return projectMember.orElseThrow(() -> new ResourceNotFoundException("project member not found"));
	}

	@Transactional(readOnly = true)
	public ProjectMember findById(Long projectId, Long userId) {
		ProjectMemberKey key = ProjectMemberKey.builder()
			.projectId(projectId)
			.userId(userId)
			.build();
		return findById(key);
	}

	@Transactional(readOnly = true)
	public List<ProjectMemberDetail> findProjectMemberDetailByProjectId(Long projectId) {
		List<ProjectMember> members = projectMemberRepository.findByProjectIdOrderByRegisterDateAsc(projectId);
		List<ProjectMemberDetail> result = modelMapper.map(members, new TypeToken<List<ProjectMemberDetail>>(){}.getType());
		return result;
	}

	@Transactional(readOnly = true)
	public List<SuggestionUserDetail> findSuggestionUserDetailByProjectId(Long projectId, String query) {
		List<User> users = userRepository.findSuggestionUserByProjectId(projectId, query);
		List<SuggestionUserDetail> result = modelMapper.map(users, new TypeToken<List<SuggestionUserDetail>>(){}.getType());
		return result;
	}

	@Transactional(rollbackFor = Exception.class)
	public ProjectMember create(ProjectMemberKey projectMemberKey, boolean isRegister) {
		ProjectMember projectMember = ProjectMember.builder()
			.id(projectMemberKey)
			.isRegister(isRegister)
			.build();
		projectMember = projectMemberRepository.save(projectMember);
		return projectMember;
	}

	@Transactional(rollbackFor = Exception.class)
	public ProjectMember create(Long projectId, Long userId, boolean isRegister) {
		ProjectMemberKey projectMemberKey = ProjectMemberKey.builder()
			.projectId(projectId)
			.userId(userId)
			.build();
		return create(projectMemberKey, isRegister);
	}

	@Transactional(rollbackFor = Exception.class)
	public void delete(Long projectId, Long userId) {
		ProjectMember member = null;

		try {
			member = findById(projectId, userId);
		} catch (ResourceNotFoundException e) {
			return;
		}

		if(member.isRegister()) {
			throw new CannotDeleteProjectOwnerException();
		}

		projectMemberRepository.delete(member);
	}
}
