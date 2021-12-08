package com.standardkim.kanban.service;

import com.standardkim.kanban.dto.TaskColumnDto.CreateTaskColumnParam;
import com.standardkim.kanban.entity.Kanban;
import com.standardkim.kanban.entity.TaskColumn;
import com.standardkim.kanban.exception.taskcolumn.DuplicateTaskColumnNameException;
import com.standardkim.kanban.exception.taskcolumn.TaskColumnNotFoundException;
import com.standardkim.kanban.repository.TaskColumnRepository;

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

import java.util.ArrayList;
import java.util.List;
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
	void findLastItemIndex_ValidColumnList_IndexOfLastItem() {
		// 4 <- 6 <- 1 <- 5 <- 2 <- 3
		List<TaskColumn> columns = new ArrayList<>();
		columns.add(getTaskColumn(1L, 6L));
		columns.add(getTaskColumn(4L, null));
		columns.add(getTaskColumn(3L, 2L));
		columns.add(getTaskColumn(6L, 4L));
		columns.add(getTaskColumn(2L, 5L));
		columns.add(getTaskColumn(5L, 1L));

		int index = taskColumnService.findLastItemIndex(columns);
		TaskColumn column = columns.get(index);

		assertThat(index).isEqualTo(2);
		assertThat(column.getId()).isEqualTo(3L);
	}

	@Test
	void findLastItemIndex_ListOnlyHasOneColumn_ReturnZeroIndex() {
		List<TaskColumn> columns = new ArrayList<>();
		columns.add(getTaskColumn(1L, null));

		int index = taskColumnService.findLastItemIndex(columns);
		TaskColumn column = columns.get(index);

		assertThat(index).isEqualTo(0);
		assertThat(column.getId()).isEqualTo(1L);
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

	private TaskColumn getTaskColumn(Long id, Long prevId) {
		return TaskColumn.builder()
			.id(id)
			.prevId(prevId)
			.build();
	}

	private Kanban getKanban(Long id) {
		return Kanban.builder()
			.id(id)
			.build();
	}
}
