package com.standardkim.kanban.service.taskcolumn;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnFindService;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnUpdateService;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;
import com.standardkim.kanban.domain.taskcolumn.dto.UpdateTaskColumnParam;
import com.standardkim.kanban.domain.taskcolumn.exception.DuplicateTaskColumnNameException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskColumnUpdateServiceTest {
	@Mock
	private TaskColumnFindService taskColumnFindService;

	@Mock
	private KanbanFindService kanbanFindService;

	@InjectMocks
	private TaskColumnUpdateService taskColumnUpdateService;

	@Test
	void update_NameIsExist_ThrowDuplicateTaskColumnNameException() {
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban(1L));
		given(taskColumnFindService.isNameExist(1L, "updated")).willReturn(true);

		assertThatThrownBy(() -> {
			taskColumnUpdateService.update(1L, 1L, 1L, getUpdateTaskColumnParam("updated"));
		}).isInstanceOf(DuplicateTaskColumnNameException.class);
	}

	@Test
	void update_NameIsNotExist_UpdateTaskColumn() {
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban(1L));
		given(taskColumnFindService.isNameExist(1L, "updated")).willReturn(false);
		given(taskColumnFindService.findById(1L)).willReturn(getTaskColumn("example"));

		TaskColumn taskColumn = taskColumnUpdateService.update(1L, 1L, 1L, getUpdateTaskColumnParam("updated"));

		assertThat(taskColumn.getName()).isEqualTo("updated");
	}

	private UpdateTaskColumnParam getUpdateTaskColumnParam(String name) {
		return UpdateTaskColumnParam.builder()
			.name(name)
			.build();
	}

	private Kanban getKanban(Long id) {
		return Kanban.builder()
			.id(id)
			.build();
	}

	private TaskColumn getTaskColumn(String name) {
		return TaskColumn.builder()
			.name(name)
			.build();
	}
}
