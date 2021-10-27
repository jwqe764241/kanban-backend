package com.standardkim.kanban.service;

import java.util.List;

import com.standardkim.kanban.dto.ProjectMemberDto.ProjectMemberDetail;
import com.standardkim.kanban.dto.UserDto.SuggestionUserDetail;
import com.standardkim.kanban.entity.ProjectMember;
import com.standardkim.kanban.entity.ProjectMemberKey;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.project.CannotDeleteProjectOwnerException;
import com.standardkim.kanban.exception.project.ProjectMemberNotFoundException;
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
	public ProjectMember create(Long projectId, Long userId, boolean isRegister) {
		ProjectMemberKey id = ProjectMemberKey.from(projectId, userId);
		ProjectMember projectMember = ProjectMember.from(id, isRegister);
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

		projectMemberRepository.delete(member);
	}
}
