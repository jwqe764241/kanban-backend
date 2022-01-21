package com.standardkim.kanban.service.taskcolumn;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnFindService;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnReorderService;
import com.standardkim.kanban.domain.taskcolumn.dao.TaskColumnRepository;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;
import com.standardkim.kanban.domain.taskcolumn.dto.ReorderTaskColumnParam;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskColumnReorderServiceTest {
	@Mock
	private TaskColumnFindService taskColumnFindService;

	@Mock
	private KanbanFindService kanbanFindService;

	@Mock
	private TaskColumnRepository taskColumnRepository;

	@InjectMocks
	private TaskColumnReorderService taskColumnReorderService;

	//move to first
	//0 - 1 - 2 - 3     --->     2 - 0 - 1 - 3
	@Test
	void reorder_MoveToFirst() {
		TaskColumn firstTaskColumn = getTaskColumn(0L);
		TaskColumn prevTaskColumn = getTaskColumn(1L);
		TaskColumn taskColumn = getTaskColumn(2L);
		TaskColumn nextTaskColumn = getTaskColumn(3L);
		prevTaskColumn.updatePrevColumn(firstTaskColumn);
		taskColumn.updatePrevColumn(prevTaskColumn);
		nextTaskColumn.updatePrevColumn(taskColumn);
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban(1L));
		given(taskColumnFindService.findById(2L)).willReturn(taskColumn);
		given(taskColumnRepository.findByPrevId(2L)).willReturn(nextTaskColumn);
		given(taskColumnRepository.findByKanbanIdAndPrevId(1L, null)).willReturn(firstTaskColumn);

		taskColumnReorderService.reorder(1L, 1L, getReorderTaskColumnParam(2L, null));

		assertThat(taskColumn.getPrevId()).isEqualTo(null);
		assertThat(firstTaskColumn.getPrevId()).isEqualTo(2L);
		assertThat(prevTaskColumn.getPrevId()).isEqualTo(0L);
		assertThat(nextTaskColumn.getPrevId()).isEqualTo(1L);
	}

	//move to last
	//0 - 1 - 2 - 3     --->     0 - 2 - 3 - 1
	@Test
	void reorder_MoveToLast() {
		TaskColumn prevTaskColumn = getTaskColumn(0L);
		TaskColumn taskColumn = getTaskColumn(1L);
		TaskColumn nextTaskColumn = getTaskColumn(2L);
		TaskColumn lastTaskColumn = getTaskColumn(3L);
		taskColumn.updatePrevColumn(prevTaskColumn);
		nextTaskColumn.updatePrevColumn(taskColumn);
		lastTaskColumn.updatePrevColumn(nextTaskColumn);
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban(1L));
		given(taskColumnFindService.findById(1L)).willReturn(taskColumn);
		given(taskColumnRepository.findByPrevId(1L)).willReturn(nextTaskColumn);
		given(taskColumnRepository.findByKanbanIdAndPrevId(1L, null)).willReturn(prevTaskColumn);
		given(taskColumnFindService.findById(3L)).willReturn(lastTaskColumn);
		given(taskColumnRepository.findByPrevId(3L)).willReturn(null);

		taskColumnReorderService.reorder(1L, 1L, getReorderTaskColumnParam(1L, 3L));

		assertThat(prevTaskColumn.getPrevId()).isEqualTo(null);
		assertThat(nextTaskColumn.getPrevId()).isEqualTo(0L);
		assertThat(lastTaskColumn.getPrevId()).isEqualTo(2L);
		assertThat(taskColumn.getPrevId()).isEqualTo(3L);
	}

	//move to forward
	//0 - 1 - 2 - 3     --->     0 - 2 - 1 - 3
	@Test
	void reorder_MoveToForward() {
		TaskColumn prevTaskColumn = getTaskColumn(0L);
		TaskColumn taskColumn = getTaskColumn(1L);
		TaskColumn nextTaskColumn = getTaskColumn(2L);
		TaskColumn lastTaskColumn = getTaskColumn(3L);
		taskColumn.updatePrevColumn(prevTaskColumn);
		nextTaskColumn.updatePrevColumn(taskColumn);
		lastTaskColumn.updatePrevColumn(nextTaskColumn);
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban(1L));
		given(taskColumnFindService.findById(1L)).willReturn(taskColumn);
		given(taskColumnRepository.findByPrevId(1L)).willReturn(nextTaskColumn);
		given(taskColumnRepository.findByKanbanIdAndPrevId(1L, null)).willReturn(prevTaskColumn);
		given(taskColumnFindService.findById(2L)).willReturn(nextTaskColumn);
		given(taskColumnRepository.findByPrevId(2L)).willReturn(lastTaskColumn);

		taskColumnReorderService.reorder(1L, 1L, getReorderTaskColumnParam(1L, 2L));

		assertThat(prevTaskColumn.getPrevId()).isEqualTo(null);
		assertThat(taskColumn.getPrevId()).isEqualTo(2L);
		assertThat(nextTaskColumn.getPrevId()).isEqualTo(0L);
		assertThat(lastTaskColumn.getPrevId()).isEqualTo(1L);
	}

	//move to backward
	//0 - 1 - 2 - 3     --->     0 - 2 - 1 - 3
	@Test
	void reorder_MoveToBackward() {
		TaskColumn firstTaskColumn = getTaskColumn(0L);
		TaskColumn prevTaskColumn = getTaskColumn(1L);
		TaskColumn taskColumn = getTaskColumn(2L);
		TaskColumn nextTaskColumn = getTaskColumn(3L);
		prevTaskColumn.updatePrevColumn(firstTaskColumn);
		taskColumn.updatePrevColumn(prevTaskColumn);
		nextTaskColumn.updatePrevColumn(taskColumn);
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban(1L));
		given(taskColumnFindService.findById(2L)).willReturn(taskColumn);
		given(taskColumnRepository.findByPrevId(2L)).willReturn(nextTaskColumn);
		given(taskColumnRepository.findByKanbanIdAndPrevId(1L, null)).willReturn(firstTaskColumn);
		given(taskColumnFindService.findById(0L)).willReturn(firstTaskColumn);
		given(taskColumnRepository.findByPrevId(0L)).willReturn(prevTaskColumn);

		taskColumnReorderService.reorder(1L, 1L, getReorderTaskColumnParam(2L, 0L));

		assertThat(firstTaskColumn.getPrevId()).isEqualTo(null);
		assertThat(prevTaskColumn.getPrevId()).isEqualTo(2L);
		assertThat(taskColumn.getPrevId()).isEqualTo(0L);
		assertThat(nextTaskColumn.getPrevId()).isEqualTo(1L);
	}

	private ReorderTaskColumnParam getReorderTaskColumnParam(Long columnId, Long prevColumnId) {
		return ReorderTaskColumnParam.builder()
			.columnId(columnId)
			.prevColumnId(prevColumnId)
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
