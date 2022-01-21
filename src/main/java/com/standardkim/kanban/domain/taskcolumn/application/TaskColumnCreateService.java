package com.standardkim.kanban.domain.taskcolumn.application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.taskcolumn.dao.TaskColumnRepository;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;
import com.standardkim.kanban.domain.taskcolumn.dto.CreateTaskColumnParam;
import com.standardkim.kanban.domain.taskcolumn.exception.DuplicateTaskColumnNameException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskColumnCreateService {
	private final TaskColumnFindService taskColumnFindService;

	private final KanbanFindService kanbanFindService;

	private final TaskColumnRepository taskColumnRepository;

	public int getLastColumnIndex(final List<TaskColumn> taskColumns) {
		int size = taskColumns.size();
		if(size == 1) {
			return 0;
		}

		Set<Long> prevIdCache = new HashSet<>();
		for(TaskColumn taskColumn : taskColumns) {
			Long prevId = taskColumn.getPrevId();
			if(prevId != null) {
				prevIdCache.add(prevId);
			}
		}

		for(int i = 0; i < size; ++i) {
			TaskColumn taskColumn = taskColumns.get(i);
			if(!prevIdCache.contains(taskColumn.getId())) {
				return i;
			}
		}

		return -1;
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public TaskColumn create(Long projectId, Long kanbanSequenceId, CreateTaskColumnParam param) {
		Kanban kanban = kanbanFindService.findByProjectIdAndSequenceId(projectId, kanbanSequenceId);

		if(taskColumnFindService.isNameExist(kanban.getId(), param.getName())) {
			throw new DuplicateTaskColumnNameException("duplicate task column name");
		}

		List<TaskColumn> taskColumns = taskColumnRepository.findByKanbanId(kanban.getId());
		TaskColumn taskColumn = null;
		if(taskColumns.isEmpty()) {
			taskColumn = TaskColumn.of(param.getName(), kanban);
		}
		else {
			int index = getLastColumnIndex(taskColumns);
			TaskColumn lastTaskColumn = taskColumns.get(index);
			taskColumn = TaskColumn.of(param.getName(), kanban, lastTaskColumn);
		}

		TaskColumn newTaskColumn = taskColumnRepository.save(taskColumn);
		return newTaskColumn;
	}
}
