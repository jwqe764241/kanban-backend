package com.standardkim.kanban.service.taskcolumn;

import java.util.Optional;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnFindService;
import com.standardkim.kanban.domain.taskcolumn.dao.TaskColumnRepository;
import com.standardkim.kanban.domain.taskcolumn.exception.TaskColumnNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskColumnFindServiceTest {
	@Mock
	private KanbanFindService kanbanFindService;

	@Mock
	private TaskColumnRepository taskColumnRepository;

	@InjectMocks
	private TaskColumnFindService taskColumnFindService;

	@Test
	void isNameExist_TaskColumnWithNameIsExist_True() {
		given(taskColumnRepository.existsByKanbanIdAndName(1L, "example")).willReturn(true);

		boolean isExist = taskColumnFindService.isNameExist(1L, "example");

		assertThat(isExist).isTrue();
	}

	@Test
	void isNameExist_TaskColumnWithNameIsNotExist_False() {
		given(taskColumnRepository.existsByKanbanIdAndName(1L, "example")).willReturn(false);

		boolean isExist = taskColumnFindService.isNameExist(1L, "example");

		assertThat(isExist).isFalse();
	}

	@Test
	void findById_TaskColumnIsNotExist_ThrowTaskColumnNotFoundException() {
		given(taskColumnRepository.findById(1L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			taskColumnFindService.findById(1L);
		}).isInstanceOf(TaskColumnNotFoundException.class);
	}

	@Test
	void findByIdAndKanbanId_TaskColumnIsNotExist_ThrowTaskColumnNotFoundException() {
		given(taskColumnRepository.findByIdAndKanbanId(1L, 1L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			taskColumnFindService.findByIdAndKanbanId(1L, 1L);
		}).isInstanceOf(TaskColumnNotFoundException.class);
	}
}
