package com.standardkim.kanban.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.standardkim.kanban.dto.TaskColumnDto.CreateTaskColumnParam;
import com.standardkim.kanban.entity.Kanban;
import com.standardkim.kanban.entity.TaskColumn;
import com.standardkim.kanban.exception.kanban.KanbanNotFoundException;
import com.standardkim.kanban.repository.KanbanRepository;
import com.standardkim.kanban.repository.TaskColumnRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskColumnService {
	private final TaskColumnRepository taskColumnRepository;

	private final KanbanRepository kanbanRepository;

	private int findLastItemIndex(final List<TaskColumn> taskColumns) {
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
	public void create(Long projectId, Long kanbanSequenceId, CreateTaskColumnParam param) {
		Kanban kanban = kanbanRepository.findByProjectIdAndSequenceId(projectId, kanbanSequenceId)
			.orElseThrow(() -> new KanbanNotFoundException("kanban not found"));
		List<TaskColumn> taskColumns = taskColumnRepository.findByKanbanId(kanban.getId());

		if(taskColumns.isEmpty()) {
			TaskColumn taskColumn = TaskColumn.from(param, kanban);
			taskColumnRepository.save(taskColumn);
		}
		else {
			int index = findLastItemIndex(taskColumns);
			TaskColumn lastTaskColumn = taskColumns.get(index);
			TaskColumn taskColumn = TaskColumn.from(param, kanban, lastTaskColumn);
			taskColumnRepository.save(taskColumn);
		}
	}

	public void moveBefore() {
		
	}

	public void moveAfter() {

	}
}
