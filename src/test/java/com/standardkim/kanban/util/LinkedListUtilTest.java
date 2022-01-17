package com.standardkim.kanban.util;

import java.util.ArrayList;
import java.util.List;

import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;
import com.standardkim.kanban.global.util.LinkedListUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.BDDAssertions.*;

@ExtendWith(MockitoExtension.class)
public class LinkedListUtilTest {
	@Test
	void findLastItemIndex_ValidColumnList_IndexOfLastItem() {
		// 4 <- 6 <- 1 <- 5 <- 2 <- 3
		List<TaskColumn> columns = new ArrayList<>();
		columns.add(getTaskColumn(1L, 6L));
		columns.add(getTaskColumn(4L, null));
		columns.add(getTaskColumn(3L, 2L));
		columns.add(getTaskColumn(6L, 4L));
		columns.add(getTaskColumn(2L, 5L));
		columns.add(getTaskColumn(5L, 1L));

		int index = LinkedListUtil.findLastItemIndex(columns);
		TaskColumn column = columns.get(index);

		assertThat(index).isEqualTo(2);
		assertThat(column.getId()).isEqualTo(3L);
	}

	@Test
	void findLastItemIndex_ListOnlyHasOneColumn_ReturnZeroIndex() {
		List<TaskColumn> columns = new ArrayList<>();
		columns.add(getTaskColumn(1L, null));

		int index = LinkedListUtil.findLastItemIndex(columns);
		TaskColumn column = columns.get(index);

		assertThat(index).isEqualTo(0);
		assertThat(column.getId()).isEqualTo(1L);
	}

	private TaskColumn getTaskColumn(Long id, Long prevId) {
		return TaskColumn.builder()
			.id(id)
			.prevId(prevId)
			.build();
	}
}
