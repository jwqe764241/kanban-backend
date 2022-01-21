package com.standardkim.kanban.service.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.standardkim.kanban.domain.project.application.ProjectFindService;
import com.standardkim.kanban.domain.project.dao.ProjectRepository;
import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.project.exception.ProjectNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProjectFindServiceTest {
	@Mock
	ProjectRepository projectRepository;

	@InjectMocks
	ProjectFindService projectFindService;

	@Test
	void isNameExist_NameIsExist_True() {
		given(projectRepository.existsByName("example")).willReturn(true);

		boolean isExist = projectFindService.isNameExist("example");

		assertThat(isExist).isTrue();
	} 

	@Test
	void isNameExist_NameIsNotExist_False() {
		given(projectRepository.existsByName("example")).willReturn(false);

		boolean isExist = projectFindService.isNameExist("example");

		assertThat(isExist).isFalse();
	}

	@Test
	void findById_ProjectIsExist_Project() {
		given(projectRepository.findById(1L)).willReturn(Optional.of(getProject(1L)));
	
		Project project = projectFindService.findById(1L);

		assertThat(project).isNotNull();
	}

	@Test
	void findById_ProjectIsNotExist_ThrowProjectNotFoundException() {
		given(projectRepository.findById(1L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			projectFindService.findById(1L);
		}).isInstanceOf(ProjectNotFoundException.class);
	}

	@Test
	void findByUserId_UserIsExist_ListOfProject() {
		given(projectRepository.findByUserId(1L)).willReturn(getProjects(3));

		List<Project> projects = projectFindService.findByUserId(1L);

		assertThat(projects).isNotNull();
	}

	private Project getProject(Long id) {
		return Project.builder()
			.id(id)
			.name("example")
			.description("example")
			.build();
	}

	private List<Project> getProjects(int size) {
		List<Project> list = new ArrayList<>();
		for(int i = 0; i < size; ++i) {
			list.add(getProject(Long.valueOf(i)));
		}
		return list;
	}
}
