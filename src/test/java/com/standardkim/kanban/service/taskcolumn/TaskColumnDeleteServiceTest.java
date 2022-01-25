package com.standardkim.kanban.service.taskcolumn;

import com.standardkim.kanban.domain.task.application.TaskDeleteService;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnDeleteService;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnFindService;
import com.standardkim.kanban.domain.taskcolumn.dao.TaskColumnRepository;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskColumnDeleteServiceTest {
	@Mock
	private TaskColumnFindService taskColumnFindService;

	@Mock
	private TaskDeleteService taskDeleteService;

	@Mock
	private TaskColumnRepository taskColumnRepository;

	@InjectMocks
	private TaskColumnDeleteService taskColumnDeleteService;

	@Test
	void delete_NoOtherColumn_DeleteColumn() {
		TaskColumn taskColumn = getTaskColumn(1L);
		given(taskColumnFindService.findById(1L)).willReturn(taskColumn);
		given(taskColumnRepository.findByPrevId(1L)).willReturn(null);

		TaskColumn updatedTaskColumn = taskColumnDeleteService.delete(1L);

		assertThat(updatedTaskColumn).isNull();
		verify(taskColumnRepository).delete(taskColumn);
	}

	@Test
	void delete_FirstColumnAndHasNextColumn_DeleteColumnAndUpdateNextColumnToFirstColumn() {
		TaskColumn taskColumn = getTaskColumn(1L);
		TaskColumn nextTaskColumn = getTaskColumn(2L);
		nextTaskColumn.updatePrevColumn(taskColumn);
		given(taskColumnFindService.findById(1L)).willReturn(taskColumn);
		given(taskColumnRepository.findByPrevId(1L)).willReturn(nextTaskColumn);

		TaskColumn updatedTaskColumn = taskColumnDeleteService.delete(1L);

		assertThat(updatedTaskColumn.getPrevTaskColumn()).isNull();
		assertThat(updatedTaskColumn.getPrevId()).isNull();
		verify(taskColumnRepository).delete(taskColumn);
	}

	@Test
	void delete_HasNextColumnAndPrevColumn_DeleteColumnAndUpdateNextColumn() {
		TaskColumn taskColumn = getTaskColumn(2L);
		TaskColumn prevTaskColumn = getTaskColumn(1L);
		TaskColumn nextTaskColumn = getTaskColumn(3L);
		taskColumn.updatePrevColumn(prevTaskColumn);
		nextTaskColumn.updatePrevColumn(taskColumn);
		given(taskColumnFindService.findById(2L)).willReturn(taskColumn);
		given(taskColumnRepository.findByPrevId(2L)).willReturn(nextTaskColumn);

		TaskColumn updatedTaskColumn = taskColumnDeleteService.delete(2L);

		assertThat(updatedTaskColumn.getPrevTaskColumn()).isEqualTo(prevTaskColumn);
		assertThat(updatedTaskColumn.getPrevId()).isEqualTo(prevTaskColumn.getId());
		verify(taskColumnRepository).delete(taskColumn);
	}

	private TaskColumn getTaskColumn(Long id) {
		return TaskColumn.builder()
			.id(id)
			.build();
	}
}
