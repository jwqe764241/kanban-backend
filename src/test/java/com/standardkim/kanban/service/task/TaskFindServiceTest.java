package com.standardkim.kanban.service.task;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.task.application.TaskFindService;
import com.standardkim.kanban.domain.task.dao.TaskRepository;
import com.standardkim.kanban.domain.task.exception.TaskNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskFindServiceTest {
	@Mock
	private KanbanFindService kanbanFindService;

	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private TaskFindService taskFindService;

	@Test
	void findById_TaskIsNotExist_ThrowTaskNotFoundException() {
		given(taskRepository.findById(1L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			taskFindService.findById(1L);
		}).isInstanceOf(TaskNotFoundException.class);
	}
}
