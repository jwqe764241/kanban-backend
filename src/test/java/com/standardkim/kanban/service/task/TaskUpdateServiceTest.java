package com.standardkim.kanban.service.task;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.task.application.TaskUpdateService;
import com.standardkim.kanban.domain.task.dao.TaskRepository;
import com.standardkim.kanban.domain.task.domain.Task;
import com.standardkim.kanban.domain.task.dto.UpdateTaskParam;
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
public class TaskUpdateServiceTest {
	@Mock
	private TaskColumnFindService taskColumnFindService;

	@Mock
	private KanbanFindService kanbanFindService;

	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private TaskUpdateService taskUpdateService;

	@Test
	void update_TaskIsExist_UpdateText() {
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban(1L));
		given(taskColumnFindService.findByIdAndKanbanId(1L, 1L)).willReturn(getTaskColumn(1L));
		given(taskRepository.findByIdAndTaskColumnId(1L, 1L)).willReturn(getTask(1L, "example"));

		Task updatedTask = taskUpdateService.update(1L, 1L, 1L, 1L, getUpdateTaskParam("updated"));

		assertThat(updatedTask.getText()).isEqualTo("updated");
	}

	private UpdateTaskParam getUpdateTaskParam(String text) {
		return UpdateTaskParam.builder()
			.text(text)
			.build();
	}

	private Task getTask(Long id, String text) {
		return Task.builder()
			.id(id)
			.text(text)
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
