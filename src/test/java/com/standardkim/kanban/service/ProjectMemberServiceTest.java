package com.standardkim.kanban.service;

import com.standardkim.kanban.dto.ProjectMemberDto.ProjectMemberDetail;
import com.standardkim.kanban.dto.UserDto.SuggestionUserDetail;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.ProjectMember;
import com.standardkim.kanban.entity.ProjectMemberKey;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.project.CannotDeleteProjectOwnerException;
import com.standardkim.kanban.exception.project.ProjectMemberNotFoundException;
import com.standardkim.kanban.repository.ProjectMemberRepository;
import com.standardkim.kanban.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.AbstractConverter;

import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProjectMemberServiceTest {
	@Mock
	ProjectMemberRepository projectMemberRepository;

	@Mock
	UserRepository userRepository;

	@Spy
	ModelMapper modelMapper = new ModelMapper();

	@InjectMocks
	ProjectMemberService projectMemberService;

	@BeforeEach
	void init() {
		modelMapper.getConfiguration()
			.setFieldAccessLevel(AccessLevel.PRIVATE)
			.setFieldMatchingEnabled(true);

		modelMapper.addConverter(new AbstractConverter<ProjectMember, ProjectMemberDetail>() {
			@Override
			public ProjectMemberDetail convert(ProjectMember projectMember) {
				User user = projectMember.getUser();
				ProjectMemberDetail projectMemberDetail = ProjectMemberDetail.builder()
					.id(user.getId())
					.name(user.getName())
					.email(user.getEmail())
					.date(projectMember.getRegisterDate())
					.build();
				return projectMemberDetail;
			}
		});
	}

	@Test
	void isExists_ProjectMemberIsExist_True() {
		given(projectMemberRepository.existsById(eq(getProjectMemberKey(1L, 1L)))).willReturn(true);

		boolean isExist = projectMemberService.isExists(1L, 1L);
		
		assertThat(isExist).isTrue();
	}

	@Test
	void isExists_ProjectMemberIsNotExist_False() {
		given(projectMemberRepository.existsById(eq(getProjectMemberKey(1L, 1L)))).willReturn(false);

		boolean isExist = projectMemberService.isExists(1L, 1L);

		assertThat(isExist).isFalse();
	}

	@Test
	void isProjectOwner_UserIsProjectOwner_True() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.of(getProjectMember(1L, 1L, true)));
	
		boolean isProjectOwner = projectMemberService.isProjectOwner(1L, 1L);

		assertThat(isProjectOwner).isTrue();
	}

	@Test
	void isProjectOwner_UserIsNotProjectOwner_False() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.of(getProjectMember(1L, 1L, false)));
	
		boolean isProjectOwner = projectMemberService.isProjectOwner(1L, 1L);

		assertThat(isProjectOwner).isFalse();
	}

	@Test
	void isProjectOwner_ProjectMemberIsNotExist_False() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.empty());
	
		boolean isProjectOwner = projectMemberService.isProjectOwner(1L, 1L);

		assertThat(isProjectOwner).isFalse();
	}

	@Test
	void findById_ProjectMemberIsExist_ProjectMember() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.of(getProjectMember(1L, 1L, true)));

		ProjectMember projectMember = projectMemberService.findById(1L, 1L);

		assertThat(projectMember).isNotNull();
	}

	@Test
	void findById_ProjectMemberIsNotExist_ThrowProjectMemberNotFoundException() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			projectMemberService.findById(1L, 1L);
		}).isInstanceOf(ProjectMemberNotFoundException.class);
	}

	@Test
	void findProjectMemberDetailByProjectId_ProjectMemberIsExist_ListOfProjectMember() {
		given(projectMemberRepository.findByProjectIdOrderByRegisterDateAsc(1L)).willReturn(getProjectMemberList(1L, 3));

		List<ProjectMemberDetail> projectDetails = projectMemberService.findProjectMemberDetailByProjectId(1L);

		assertThat(projectDetails).hasSize(3);
	}
	
	@Test
	void findProjectMemberDetailByProjectId_ProjectMemberIsNotExist_EmptyList() {
		given(projectMemberRepository.findByProjectIdOrderByRegisterDateAsc(1L)).willReturn(new ArrayList<ProjectMember>());

		List<ProjectMemberDetail> projectDetails = projectMemberService.findProjectMemberDetailByProjectId(1L);

		assertThat(projectDetails).isEmpty();
	}

	@Test
	void findSuggestionUserDetailByProjectId_UserIsExist_ListOfSuggestionUserDetail() {
		given(userRepository.findSuggestionUserByProjectId(eq(1L), anyString())).willReturn(getUserList(3));
	
		List<SuggestionUserDetail> list = projectMemberService.findSuggestionUserDetailByProjectId(1L, "a");

		assertThat(list).hasSize(3);
	}

	@Test
	void findSuggestionUserDetailByProjectId_UserIsNotExist_EmptyList() {
		given(userRepository.findSuggestionUserByProjectId(eq(1L), anyString())).willReturn(new ArrayList<User>());
	
		List<SuggestionUserDetail> list = projectMemberService.findSuggestionUserDetailByProjectId(1L, "a");

		assertThat(list).isEmpty();
	}

	@Test
	void create_ProjectMemberIsNotExist_() {
		given(projectMemberRepository.save(any(ProjectMember.class))).willReturn(getProjectMember(1L, 1L, true));

		ProjectMember projectMember = projectMemberService.create(1L, 1L, true);

		assertThat(projectMember).isNotNull();
	}
	
	@Disabled("disabled until implement this feature")
	@Test
	void create_ProjectMemberIsExist_ThrowProjectMemberDuplicateException() {
		
	}

	@Test
	void delete_ProjectMemberIsNotExist_DoesNotThrowAnyException() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.empty());

		assertThatCode(() -> {
			projectMemberService.delete(1L, 1L);
		}).doesNotThrowAnyException();
	}

	@Test
	void delete_ProjectMemberIsProjectOwner_ThrowCannotDeleteProjectOwnerException() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.of(getProjectMember(1L, 1L, true)));

		assertThatThrownBy(() -> {
			projectMemberService.delete(1L, 1L);
		}).isInstanceOf(CannotDeleteProjectOwnerException.class);
	}

	private ProjectMemberKey getProjectMemberKey(Long projectId, Long userId) {
		return ProjectMemberKey.builder()
			.projectId(projectId)
			.userId(userId)
			.build();
	}

	private ProjectMember getProjectMember(Long projectId, Long userId, boolean isRegister) {
		ProjectMemberKey id = getProjectMemberKey(projectId, userId);
		return ProjectMember.builder()
			.id(id)
			.project(Project.builder()
				.id(projectId)
				.build()
			)
			.user(User.builder()
				.id(userId)
				.build()
			)
			.isRegister(isRegister)
			.build();
	}

	private List<User> getUserList(int size) {
		ArrayList<User> list = new ArrayList<>(size);
		for(int i = 1; i <= size; ++i) {
			list.add(User.builder().id(Long.valueOf(i)).build());
		}
		return list;
	}

	private List<ProjectMember> getProjectMemberList(Long projectId, int size) {
		ArrayList<ProjectMember> list = new ArrayList<>(size);
		for(int i = 1; i <= size; ++i) {
			list.add(getProjectMember(projectId, Long.valueOf(i), i  == 1));
		}
		return list;
	}
}
