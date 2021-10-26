package com.standardkim.kanban.service;

import static org.mockito.ArgumentMatchers.anyString;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.dto.ProjectDto.CreateProjectParam;
import com.standardkim.kanban.dto.ProjectDto.ProjectDetail;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.ProjectMember;
import com.standardkim.kanban.entity.ProjectMemberKey;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.project.DuplicateProjectNameException;
import com.standardkim.kanban.exception.project.ProjectNotFoundException;
import com.standardkim.kanban.exception.user.UserNotFoundException;
import com.standardkim.kanban.repository.ProjectRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
	User testUser;
	SecurityUser testSecurityUser;
	Project testProject;
	ProjectMember testProjectMember;

	@Mock
	ProjectRepository projectRepository;

	@Mock
	ProjectMemberService projectMemberService;

	@Mock
	UserService userService;

	@Spy
	ModelMapper modelMapper = new ModelMapper();

	@InjectMocks
	ProjectService projectService;

	@BeforeEach
	void init() {
		modelMapper.getConfiguration()
			.setFieldAccessLevel(AccessLevel.PRIVATE)
			.setFieldMatchingEnabled(true);

		testUser = getUser();
		testSecurityUser = getSecurityUser();
		testProject = getProject();
		testProjectMember = getProjectMember();
	}

	@Test
	void isProjectNameExists_ProjectNameIsExist_True() {
		given(projectRepository.existsByName(anyString())).willReturn(true);

		boolean isExist = projectService.isProjectNameExists("");

		assertThat(isExist).isTrue();
	} 

	@Test
	void isProjectNameExists_ProjectNameIsNotExist_False() {
		given(projectRepository.existsByName(anyString())).willReturn(false);

		boolean isExist = projectService.isProjectNameExists("");

		assertThat(isExist).isFalse();
	}

	@Test
	void findById_ProjectIsExist_Project() {
		given(projectRepository.findById(anyLong())).willReturn(Optional.of(testProject));
	
		Project project = projectService.findById(1L);

		assertThat(project).isNotNull();
	}

	@Test
	void findById_ProjectIsNotExist_ThrowProjectNotFoundException() {
		given(projectRepository.findById(anyLong())).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			projectService.findById(1L);
		}).isInstanceOf(ProjectNotFoundException.class);
	}

	@Test
	void findByUserId_UserIsExist_ListOfProject() {
		given(userService.findById(anyLong())).willReturn(testUser);

		List<Project> projects = projectService.findByUserId(1L);

		assertThat(projects).isNotNull();
	}

	@Test
	void findByUserId_UserIsNotExist_ThrowUserNotFoundException() {
		given(userService.findById(anyLong())).willThrow(new UserNotFoundException("user not found"));

		assertThatThrownBy(() -> {
			projectService.findByUserId(1L);
		}).isInstanceOf(UserNotFoundException.class);
	}

	@Test
	void findBySecurityUser_UserIsExist_ListOfProject() {
		given(userService.findBySecurityUser()).willReturn(testUser);
		given(userService.findById(anyLong())).willReturn(testUser);

		List<Project> projects = projectService.findBySecurityUser();

		assertThat(projects).isNotNull();
	}

	@Test
	void findBySecurityUser_UserIsNotExist_ThrowUserNotFoundException() {
		given(userService.findBySecurityUser()).willThrow(new UserNotFoundException("user not found"));
	
		assertThatThrownBy(() -> {
			projectService.findBySecurityUser();
		}).isInstanceOf(UserNotFoundException.class);
	}

	@Test
	void findProjectDetailBySecurityUser_UserIsExist_ListOfProjectDetail() {
		given(userService.findBySecurityUser()).willReturn(testUser);
		given(userService.findById(anyLong())).willReturn(testUser);

		List<ProjectDetail> projectDetails = projectService.findProjectDetailBySecurityUser();

		assertThat(projectDetails).isNotNull();
	}

	@Test
	void findProjectDetailBySecurityUser_UserIsNotExist_ThrowUserNotFoundException() {
		given(userService.findBySecurityUser()).willThrow(new UserNotFoundException("user not found"));

		assertThatThrownBy(() -> {
			projectService.findProjectDetailBySecurityUser();
		}).isInstanceOf(UserNotFoundException.class);
	}

	@Test
	void findProjectDetailById_ProjectIsExist_ProjectDetail() {
		given(projectRepository.findById(anyLong())).willReturn(Optional.of(testProject));

		ProjectDetail projectDetail = projectService.findProjectDetailById(1L);

		assertThat(projectDetail).isNotNull();
	}

	@Test
	void findProjectDetailById_ProjectIsNotExist_ThrowProjectNotFoundException() {
		given(projectRepository.findById(anyLong())).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			projectService.findProjectDetailById(1L);
		}).isInstanceOf(ProjectNotFoundException.class);
	}

	@Test
	void create_ProjectNameIsNotExist_Project() {
		given(projectRepository.existsByName(anyString())).willReturn(false);
		given(userService.findBySecurityUser()).willReturn(testUser);
		given(projectRepository.save(any(Project.class))).willReturn(testProject);
		given(projectMemberService.create(anyLong(), anyLong(), eq(true))).willReturn(testProjectMember);

		Project project = projectService.create(getCreateProjectParam());

		assertThat(project).isNotNull();
	}

	@Test
	void create_ProjectNameIsExist_ThrownDuplicateProjectNameException() {
		given(projectRepository.existsByName(anyString())).willReturn(true);

		assertThatThrownBy(() -> {
			projectService.create(getCreateProjectParam());
		}).isInstanceOf(DuplicateProjectNameException.class);
	}

	private User getUser() {
		return User.builder()
			.id(1L)
			.login("example")
			.password("$2a$10$bblgiOA1X6W06J0kyBm1ie13O8o5Q2rm4C4.oZyA8e55PNP7OAXKu")
			.name("example")
			.email("example@example.com")
			.registerDate(LocalDateTime.of(2021, 7, 1, 22, 0, 0, 0))
			.projects(new HashSet<ProjectMember>())
			.build();
	}

	private SecurityUser getSecurityUser() {
		return SecurityUser.builder()
			.id(1L)
			.login("example")
			.password("$2a$10$bblgiOA1X6W06J0kyBm1ie13O8o5Q2rm4C4.oZyA8e55PNP7OAXKu")
			.name("example")
			.registerDate(LocalDateTime.of(2021, 7, 1, 22, 0, 0, 0))
			.build();
	}

	private Project getProject() {
		return Project.builder()
			.id(1L)
			.name("example")
			.description("example")
			.registerUser(testUser)
			.registerDate(LocalDateTime.of(2021, 7, 1, 22, 0, 0, 0))
			.build();
	}

	private ProjectMember getProjectMember() {
		ProjectMemberKey key = ProjectMemberKey.builder()
			.userId(1L)
			.projectId(1L)
			.build();

		return ProjectMember.builder()
			.id(key)
			.user(getUser())
			.project(getProject())
			.isRegister(true)
			.registerDate(LocalDateTime.of(2021, 7, 1, 22, 0, 0, 0))
			.build();
	}

	private CreateProjectParam getCreateProjectParam() {
		return CreateProjectParam.builder()
			.name("example")
			.description("example")
			.build();
	}
}
