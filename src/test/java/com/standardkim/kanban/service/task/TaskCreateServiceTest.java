package com.standardkim.kanban.service.task;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.task.application.TaskCreateService;
import com.standardkim.kanban.domain.task.dao.TaskRepository;
import com.standardkim.kanban.domain.task.domain.Task;
import com.standardkim.kanban.domain.task.dto.CreateTaskParam;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnFindService;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskCreateServiceTest {
	@Mock
	private TaskColumnFindService taskColumnFindService;

	@Mock
	private KanbanFindService kanbanFindService;

	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private TaskCreateService taskCreateService;

	@Test
	void create_FirstTaskIsExist_UpdatePrevTaskofFirstTaskToNewTask() {
		Task firstTask = getTask(1L);
		Task newTask = getTask(2L);
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban(1L));
		given(taskColumnFindService.findByIdAndKanbanId(1L, 1L)).willReturn(getTaskColumn(1L));
		given(taskRepository.findByPrevIdAndTaskColumnId(null, 1L)).willReturn(firstTask);
		given(taskRepository.save(any())).willReturn(newTask);

		taskCreateService.create(1L, 1L, 1L, getCreateTaskParam());

		assertThat(firstTask.getPrevId()).isEqualTo(2L);
	}

	private CreateTaskParam getCreateTaskParam() {
		return CreateTaskParam.builder()
			.build();
	}

	private Task getTask(Long id) {
		return Task.builder()
			.id(id)
			.build();
	}

	private Kanban getKanban(Long id) {
		return Kanban.builder()
			.id(id)
			.build();
	}

	private TaskColumn getTaskColumn(Long id) {
		return TaskColumn.builder()
			.id(id)
			.build();
	}
}
