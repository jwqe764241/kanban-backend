package com.standardkim.kanban.service;

import com.standardkim.kanban.domain.kanban.application.KanbanService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnService;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumnRepository;
import com.standardkim.kanban.domain.taskcolumn.exception.DuplicateTaskColumnNameException;
import com.standardkim.kanban.domain.taskcolumn.exception.TaskColumnNotFoundException;
import com.standardkim.kanban.domain.taskcolumn.presentation.TaskColumnDto.CreateTaskColumnParam;

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

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskColumnServiceTest {
	@Mock
	TaskColumnRepository taskColumnRepository;
	
	@Mock
	KanbanService kanbanService;

	@Spy
	ModelMapper modelMapper;

	@InjectMocks
	TaskColumnService taskColumnService;

	@BeforeEach
	public void init() {
		modelMapper.getConfiguration()
			.setFieldAccessLevel(AccessLevel.PRIVATE)
			.setFieldMatchingEnabled(true);
	}

	@Test
	void isNameExist_TaskColumnWithNameIsExist_True() {
		given(taskColumnRepository.existsByKanbanIdAndName(1L, "example")).willReturn(true);

		boolean isExist = taskColumnService.isNameExist(1L, "example");

		assertThat(isExist).isTrue();
	}

	@Test
	void isNameExist_TaskColumnWithNameIsNotExist_False() {
		given(taskColumnRepository.existsByKanbanIdAndName(1L, "example")).willReturn(false);

		boolean isExist = taskColumnService.isNameExist(1L, "example");

		assertThat(isExist).isFalse();
	}

	@Test
	void findById_TaskColumnIsNotExist_ThrowTaskColumnNotFoundException() {
		given(taskColumnRepository.findById(1L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			taskColumnService.findById(1L);
		}).isInstanceOf(TaskColumnNotFoundException.class);
	}

	@Test
	void findByIdAndKanbanId_TaskColumnIsNotExist_ThrowTaskColumnNotFoundException() {
		given(taskColumnRepository.findByIdAndKanbanId(1L, 1L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			taskColumnService.findByIdAndKanbanId(1L, 1L);
		}).isInstanceOf(TaskColumnNotFoundException.class);
	}

	@Test
	void create_NameIsExist_ThrowDuplicateTaskColumnNameException() {
		given(kanbanService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban(1L));
		given(taskColumnRepository.existsByKanbanIdAndName(1L, "example")).willReturn(true);

		assertThatThrownBy(() -> {
			taskColumnService.create(1L, 1L, getCreateTaskColumnParam("example"));
		}).isInstanceOf(DuplicateTaskColumnNameException.class);
	}

	private CreateTaskColumnParam getCreateTaskColumnParam(String name) {
		return CreateTaskColumnParam.builder()
			.name(name)
			.build();
	}

	private Kanban getKanban(Long id) {
		return Kanban.builder()
			.id(id)
			.build();
	}
}
