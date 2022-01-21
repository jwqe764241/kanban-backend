package com.standardkim.kanban.service.task;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.task.application.TaskFindService;
import com.standardkim.kanban.domain.task.application.TaskReorderService;
import com.standardkim.kanban.domain.task.dao.TaskRepository;
import com.standardkim.kanban.domain.task.domain.Task;
import com.standardkim.kanban.domain.task.dto.ReorderTaskParam;
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
public class TaskReorderServiceTest {
	@Mock
	private TaskFindService taskFindService;

	@Mock
	private TaskColumnFindService taskColumnFindService;

	@Mock
	private KanbanFindService kanbanFindService;

	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private TaskReorderService taskReorderService;

	//move to first
	//0 - 1 - 2 - 3     -->     2 - 0 - 1 - 3
	@Test
	void reorder_MoveToFirst() {
		TaskColumn taskColumn = getTaskColumn(1L);
		Task firstTask = getTask(0L, taskColumn);
		Task prevTask = getTask(1L, taskColumn);
		Task task = getTask(2L, taskColumn);
		Task nextTask = getTask(3L, taskColumn);
		prevTask.updatePrevTask(firstTask);
		task.updatePrevTask(prevTask);
		nextTask.updatePrevTask(task);
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban(1L));
		given(taskFindService.findById(2L)).willReturn(task);
		given(taskColumnFindService.findByIdAndKanbanId(1L, 1L)).willReturn(taskColumn);
		given(taskRepository.findByPrevIdAndTaskColumnId(2L, 1L)).willReturn(nextTask);
		given(taskRepository.findByPrevIdAndTaskColumnId(null, 1L)).willReturn(firstTask);

		taskReorderService.reorder(1L, 1L, 1L, getReorderTaskParam(2L, null));

		assertThat(task.getPrevId()).isNull();
		assertThat(firstTask.getPrevId()).isEqualTo(2L);
		assertThat(prevTask.getPrevId()).isEqualTo(0L);
		assertThat(nextTask.getPrevId()).isEqualTo(1L);
	}

	//move to last
	//0 - 1 - 2 - 3     --->     0 - 2 - 3 - 1
	@Test
	void reorder_MoveToLast() {
		TaskColumn taskColumn = getTaskColumn(1L);
		Task prevTask = getTask(0L, taskColumn);
		Task task = getTask(1L, taskColumn);
		Task nextTask = getTask(2L, taskColumn);
		Task lastTask = getTask(3L, taskColumn);
		task.updatePrevTask(prevTask);
		nextTask.updatePrevTask(task);
		lastTask.updatePrevTask(nextTask);
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban(1L));
		given(taskFindService.findById(1L)).willReturn(task);
		given(taskColumnFindService.findByIdAndKanbanId(1L, 1L)).willReturn(taskColumn);
		given(taskRepository.findByPrevIdAndTaskColumnId(1L, 1L)).willReturn(nextTask);
		given(taskRepository.findByPrevIdAndTaskColumnId(null, 1L)).willReturn(prevTask);
		given(taskFindService.findById(3L)).willReturn(lastTask);
		given(taskRepository.findByPrevIdAndTaskColumnId(3L, 1L)).willReturn(null);

		taskReorderService.reorder(1L, 1L, 1L, getReorderTaskParam(1L, 3L));

		assertThat(prevTask.getPrevId()).isNull();
		assertThat(nextTask.getPrevId()).isEqualTo(0L);
		assertThat(lastTask.getPrevId()).isEqualTo(2L);
		assertThat(task.getPrevId()).isEqualTo(3L);
	}

	//move to forward
	//0 - 1 - 2 - 3     --->     0 - 2 - 1 - 3
	@Test
	void reorder_MoveToForward() {
		TaskColumn taskColumn = getTaskColumn(1L);
		Task firstTask = getTask(0L, taskColumn);
		Task prevTask = getTask(1L, taskColumn);
		Task task = getTask(2L, taskColumn);
		Task nextTask = getTask(3L, taskColumn);
		prevTask.updatePrevTask(firstTask);
		task.updatePrevTask(prevTask);
		nextTask.updatePrevTask(task);
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban(1L));
		given(taskFindService.findById(2L)).willReturn(task);
		given(taskColumnFindService.findByIdAndKanbanId(1L, 1L)).willReturn(taskColumn);
		given(taskRepository.findByPrevIdAndTaskColumnId(2L, 1L)).willReturn(nextTask);
		given(taskRepository.findByPrevIdAndTaskColumnId(null, 1L)).willReturn(firstTask);
		given(taskFindService.findById(0L)).willReturn(firstTask);
		given(taskRepository.findByPrevIdAndTaskColumnId(0L, 1L)).willReturn(prevTask);

		taskReorderService.reorder(1L, 1L, 1L, getReorderTaskParam(2L, 0L));

		assertThat(firstTask.getPrevId()).isNull();
		assertThat(task.getPrevId()).isEqualTo(0L);
		assertThat(prevTask.getPrevId()).isEqualTo(2L);
		assertThat(nextTask.getPrevId()).isEqualTo(1L);
	}

	//move to backward
	//0 - 1 - 2 - 3     --->     0 - 2 - 1 - 3
	@Test
	void reorder_MoveToBackward() {
		TaskColumn taskColumn = getTaskColumn(1L);
		Task prevTask = getTask(0L, taskColumn);
		Task task = getTask(1L, taskColumn);
		Task nextTask = getTask(2L, taskColumn);
		Task lastTask = getTask(3L, taskColumn);
		task.updatePrevTask(prevTask);
		nextTask.updatePrevTask(task);
		lastTask.updatePrevTask(nextTask);
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban(1L));
		given(taskFindService.findById(1L)).willReturn(task);
		given(taskColumnFindService.findByIdAndKanbanId(1L, 1L)).willReturn(taskColumn);
		given(taskRepository.findByPrevIdAndTaskColumnId(1L, 1L)).willReturn(nextTask);
		given(taskRepository.findByPrevIdAndTaskColumnId(null, 1L)).willReturn(prevTask);
		given(taskFindService.findById(2L)).willReturn(nextTask);
		given(taskRepository.findByPrevIdAndTaskColumnId(2L, 1L)).willReturn(lastTask);

		taskReorderService.reorder(1L, 1L, 1L, getReorderTaskParam(1L, 2L));

		assertThat(prevTask.getPrevId()).isNull();
		assertThat(nextTask.getPrevId()).isEqualTo(0L);
		assertThat(task.getPrevId()).isEqualTo(2L);
		assertThat(lastTask.getPrevId()).isEqualTo(1L);
	}

	//move to other column's first
	//0 - 1 - 2     --->     0 - 2
	//3                      1 - 3
	@Test
	void reorder_MoveToFirstOfOtherColumn() {
		TaskColumn taskColumn = getTaskColumn(0L);
		TaskColumn otherTaskColumn = getTaskColumn(1L);
		Task prevTask = getTask(0L, taskColumn);
		Task task = getTask(1L, taskColumn);
		Task nextTask = getTask(2L, taskColumn);
		Task otherColumnTask = getTask(3L, otherTaskColumn);
		task.updatePrevTask(prevTask);
		nextTask.updatePrevTask(task);
		given(kanbanFindService.findByProjectIdAndSequenceId(0L, 0L)).willReturn(getKanban(0L));
		given(taskFindService.findById(task.getId())).willReturn(task);
		given(taskColumnFindService.findByIdAndKanbanId(otherTaskColumn.getId(), 0L)).willReturn(otherTaskColumn);
		given(taskRepository.findByPrevIdAndTaskColumnId(task.getId(), taskColumn.getId())).willReturn(nextTask);
		given(taskRepository.findByPrevIdAndTaskColumnId(null, otherTaskColumn.getId())).willReturn(otherColumnTask);

		taskReorderService.reorder(0L, 0L, otherTaskColumn.getId(), getReorderTaskParam(task.getId(), null));

		assertThat(prevTask.getPrevId()).isNull();
		assertThat(prevTask.getTaskColumnId()).isEqualTo(taskColumn.getId());
		assertThat(nextTask.getPrevId()).isEqualTo(prevTask.getId());
		assertThat(nextTask.getTaskColumnId()).isEqualTo(taskColumn.getId());

		assertThat(task.getPrevId()).isNull();
		assertThat(task.getTaskColumnId()).isEqualTo(otherTaskColumn.getId());
		assertThat(otherColumnTask.getPrevId()).isEqualTo(task.getId());
		assertThat(otherColumnTask.getTaskColumnId()).isEqualTo(otherTaskColumn.getId());
	}

	//move to other columns's last
	//0 - 1 - 2     --->     0 - 2
	//3                      3 - 1
	@Test
	void reorder_MoveToLastOfOtherColumn() {
		TaskColumn taskColumn = getTaskColumn(0L);
		TaskColumn otherTaskColumn = getTaskColumn(1L);
		Task prevTask = getTask(0L, taskColumn);
		Task task = getTask(1L, taskColumn);
		Task nextTask = getTask(2L, taskColumn);
		Task otherColumnTask = getTask(3L, otherTaskColumn);
		task.updatePrevTask(prevTask);
		nextTask.updatePrevTask(task);
		given(kanbanFindService.findByProjectIdAndSequenceId(0L, 0L)).willReturn(getKanban(0L));
		given(taskFindService.findById(task.getId())).willReturn(task);
		given(taskColumnFindService.findByIdAndKanbanId(otherTaskColumn.getId(), 0L)).willReturn(otherTaskColumn);
		given(taskRepository.findByPrevIdAndTaskColumnId(task.getId(), taskColumn.getId())).willReturn(nextTask);
		given(taskRepository.findByPrevIdAndTaskColumnId(null, otherTaskColumn.getId())).willReturn(otherColumnTask);
		given(taskFindService.findById(otherColumnTask.getId())).willReturn(otherColumnTask);
		given(taskRepository.findByPrevIdAndTaskColumnId(otherColumnTask.getId(), otherTaskColumn.getId())).willReturn(null);

		taskReorderService.reorder(0L, 0L, otherTaskColumn.getId(), getReorderTaskParam(task.getId(), otherColumnTask.getId()));

		assertThat(prevTask.getPrevId()).isNull();
		assertThat(prevTask.getTaskColumnId()).isEqualTo(taskColumn.getId());
		assertThat(nextTask.getPrevId()).isEqualTo(prevTask.getId());
		assertThat(nextTask.getTaskColumnId()).isEqualTo(taskColumn.getId());

		assertThat(otherColumnTask.getPrevId()).isNull();
		assertThat(otherColumnTask.getTaskColumnId()).isEqualTo(otherTaskColumn.getId());
		assertThat(task.getPrevId()).isEqualTo(otherColumnTask.getId());
		assertThat(task.getTaskColumnId()).isEqualTo(otherTaskColumn.getId());
	}
	
	//move to other column
	//0 - 1 - 2     --->     0 - 2
	//3 - 4                  3 - 1 - 4
	@Test
	void reorder_MoveToOtherColumn() {
		TaskColumn taskColumn = getTaskColumn(0L);
		TaskColumn otherTaskColumn = getTaskColumn(1L);
		Task prevTask = getTask(0L, taskColumn);
		Task task = getTask(1L, taskColumn);
		Task nextTask = getTask(2L, taskColumn);
		Task otherColumnTask = getTask(3L, otherTaskColumn);
		Task otherColumnNextTask = getTask(4L, otherTaskColumn);
		task.updatePrevTask(prevTask);
		nextTask.updatePrevTask(task);
		otherColumnNextTask.updatePrevTask(otherColumnTask);
		given(kanbanFindService.findByProjectIdAndSequenceId(0L, 0L)).willReturn(getKanban(0L));
		given(taskFindService.findById(task.getId())).willReturn(task);
		given(taskColumnFindService.findByIdAndKanbanId(otherTaskColumn.getId(), 0L)).willReturn(otherTaskColumn);
		given(taskRepository.findByPrevIdAndTaskColumnId(task.getId(), taskColumn.getId())).willReturn(nextTask);
		given(taskRepository.findByPrevIdAndTaskColumnId(null, otherTaskColumn.getId())).willReturn(otherColumnTask);
		given(taskFindService.findById(otherColumnTask.getId())).willReturn(otherColumnTask);
		given(taskRepository.findByPrevIdAndTaskColumnId(otherColumnTask.getId(), otherTaskColumn.getId())).willReturn(otherColumnNextTask);

		taskReorderService.reorder(0L, 0L, otherTaskColumn.getId(), getReorderTaskParam(task.getId(), otherColumnTask.getId()));

		assertThat(prevTask.getPrevId()).isNull();
		assertThat(prevTask.getTaskColumnId()).isEqualTo(taskColumn.getId());
		assertThat(nextTask.getPrevId()).isEqualTo(prevTask.getId());
		assertThat(nextTask.getTaskColumnId()).isEqualTo(taskColumn.getId());

		assertThat(otherColumnTask.getPrevId()).isNull();
		assertThat(otherColumnTask.getTaskColumnId()).isEqualTo(otherTaskColumn.getId());
		assertThat(task.getPrevId()).isEqualTo(otherColumnTask.getId());
		assertThat(task.getTaskColumnId()).isEqualTo(otherTaskColumn.getId());
		assertThat(otherColumnNextTask.getPrevId()).isEqualTo(task.getId());
		assertThat(otherColumnNextTask.getTaskColumnId()).isEqualTo(otherTaskColumn.getId());
	}

	private ReorderTaskParam getReorderTaskParam(Long taskId, Long prevTaskId) {
		return ReorderTaskParam.builder()
			.taskId(taskId)
			.prevTaskId(prevTaskId)
			.build();
	}

	private Task getTask(Long id, TaskColumn taskColumn) {
		return Task.builder()
			.id(id)
			.taskColumn(taskColumn)
			.taskColumnId(taskColumn.getId())
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
