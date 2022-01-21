package com.standardkim.kanban.service.task;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.task.application.TaskDeleteService;
import com.standardkim.kanban.domain.task.dao.TaskRepository;
import com.standardkim.kanban.domain.task.domain.Task;
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
public class TaskDeleteServiceTest {
	@Mock
	private TaskColumnFindService taskColumnFindService;

	@Mock
	private KanbanFindService kanbanFindService;

	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private TaskDeleteService taskDeleteService;

	@Test
	void delete_DeleteFirstTask() {
		Task firstTask = getTask(0L);
		Task task = getTask(1L);
		Task lastTask = getTask(2L);
		task.updatePrevTask(firstTask);
		lastTask.updatePrevTask(task);
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban(1L));
		given(taskColumnFindService.findByIdAndKanbanId(1L, 1L)).willReturn(getTaskColumn(1L));
		given(taskRepository.findByIdAndTaskColumnId(0L, 1L)).willReturn(firstTask);
		given(taskRepository.findByPrevIdAndTaskColumnId(0L, 1L)).willReturn(task);

		taskDeleteService.delete(1L, 1L, 1L, 0L);

		assertThat(task.getPrevId()).isNull();
		assertThat(lastTask.getPrevId()).isEqualTo(1L);
		verify(taskRepository).delete(firstTask);
	}

	@Test
	void delete_DeleteLastTask() {
		Task firstTask = getTask(0L);
		Task task = getTask(1L);
		Task lastTask = getTask(2L);
		task.updatePrevTask(firstTask);
		lastTask.updatePrevTask(task);
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban(1L));
		given(taskColumnFindService.findByIdAndKanbanId(1L, 1L)).willReturn(getTaskColumn(1L));
		given(taskRepository.findByIdAndTaskColumnId(2L, 1L)).willReturn(lastTask);
		given(taskRepository.findByPrevIdAndTaskColumnId(2L, 1L)).willReturn(null);

		taskDeleteService.delete(1L, 1L, 1L, 2L);

		assertThat(firstTask.getPrevId()).isNull();
		assertThat(task.getPrevId()).isEqualTo(0L);
		verify(taskRepository).delete(lastTask);
	}

	@Test
	void delete_DeleteTask() {
		Task firstTask = getTask(0L);
		Task task = getTask(1L);
		Task lastTask = getTask(2L);
		task.updatePrevTask(firstTask);
		lastTask.updatePrevTask(task);
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban(1L));
		given(taskColumnFindService.findByIdAndKanbanId(1L, 1L)).willReturn(getTaskColumn(1L));
		given(taskRepository.findByIdAndTaskColumnId(1L, 1L)).willReturn(task);
		given(taskRepository.findByPrevIdAndTaskColumnId(1L, 1L)).willReturn(lastTask);

		taskDeleteService.delete(1L, 1L, 1L, 1L);

		assertThat(firstTask.getPrevId()).isNull();
		assertThat(lastTask.getPrevId()).isEqualTo(0L);
		verify(taskRepository).delete(task);

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
