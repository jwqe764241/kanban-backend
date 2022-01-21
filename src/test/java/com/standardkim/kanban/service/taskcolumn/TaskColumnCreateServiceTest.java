package com.standardkim.kanban.service.taskcolumn;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnCreateService;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnFindService;
import com.standardkim.kanban.domain.taskcolumn.dao.TaskColumnRepository;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;
import com.standardkim.kanban.domain.taskcolumn.dto.CreateTaskColumnParam;
import com.standardkim.kanban.domain.taskcolumn.exception.DuplicateTaskColumnNameException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TaskColumnCreateServiceTest {
	@Mock
	private TaskColumnFindService taskColumnFindService;

	@Mock
	private KanbanFindService kanbanFindService;

	@Mock
	private TaskColumnRepository taskColumnRepository;

	@InjectMocks
	private TaskColumnCreateService taskColumnCreateService;

	@Test
	void getLastColumnIndex_ValidColumnList_IndexOfLastItem() {
		// 4 <- 6 <- 1 <- 5 <- 2 <- 3
		List<TaskColumn> columns = new ArrayList<>();
		columns.add(getTaskColumn(1L, 6L));
		columns.add(getTaskColumn(4L, null));
		columns.add(getTaskColumn(3L, 2L));
		columns.add(getTaskColumn(6L, 4L));
		columns.add(getTaskColumn(2L, 5L));
		columns.add(getTaskColumn(5L, 1L));

		int index = taskColumnCreateService.getLastColumnIndex(columns);
		TaskColumn column = columns.get(index);

		assertThat(index).isEqualTo(2);
		assertThat(column.getId()).isEqualTo(3L);
	}

	@Test
	void getLastColumnIndex_ListOnlyHasOneColumn_ReturnZeroIndex() {
		List<TaskColumn> columns = new ArrayList<>();
		columns.add(getTaskColumn(1L, null));

		int index = taskColumnCreateService.getLastColumnIndex(columns);
		TaskColumn column = columns.get(index);

		assertThat(index).isEqualTo(0);
		assertThat(column.getId()).isEqualTo(1L);
	}

	@Test
	void create_NameIsExist_ThrowDuplicateTaskColumnNameException() {
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban(1L));
		given(taskColumnFindService.isNameExist(1L, "example")).willReturn(true);

		assertThatThrownBy(() -> {
			taskColumnCreateService.create(1L, 1L, getCreateTaskColumnParam("example"));
		}).isInstanceOf(DuplicateTaskColumnNameException.class);
	}

	private CreateTaskColumnParam getCreateTaskColumnParam(String name) {
		return CreateTaskColumnParam.builder()
			.name(name)
			.build();
	}

	private Kanban getKanban(Long id) {
		return Kanban.builder()
			.id(id)
			.build();
	}

	private TaskColumn getTaskColumn(Long id, Long prevId) {
		return TaskColumn.builder()
			.id(id)
			.prevId(prevId)
			.build();
	}
}
