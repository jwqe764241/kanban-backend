package com.standardkim.kanban.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.standardkim.kanban.dto.TaskColumnDto.CreateTaskColumnParam;
import com.standardkim.kanban.dto.TaskColumnDto.TaskColumnDetail;
import com.standardkim.kanban.entity.Kanban;
import com.standardkim.kanban.entity.TaskColumn;
import com.standardkim.kanban.exception.kanban.KanbanNotFoundException;
import com.standardkim.kanban.exception.taskcolumn.DuplicateTaskColumnNameException;
import com.standardkim.kanban.repository.KanbanRepository;
import com.standardkim.kanban.repository.TaskColumnRepository;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskColumnService {
	private final TaskColumnRepository taskColumnRepository;

	private final KanbanRepository kanbanRepository;

	private final ModelMapper modelMapper;

	@Transactional(readOnly = true)
	public boolean isNameExist(Long kanbanId, String name) {
		return taskColumnRepository.existsByKanbanIdAndName(kanbanId, name);
	}

	@Transactional(readOnly = true)
	public List<TaskColumnDetail> findTaskColumnDetails(Long projectId, Long kanbanSequenceId) {
		Kanban kanban = kanbanRepository.findByProjectIdAndSequenceId(projectId, kanbanSequenceId)
			.orElseThrow(() -> new KanbanNotFoundException("kanban not found"));
		List<TaskColumn> taskColumns = taskColumnRepository.findByKanbanId(kanban.getId());
		List<TaskColumnDetail> taskColumnDetails = modelMapper.map(taskColumns, new TypeToken<List<TaskColumnDetail>>(){}.getType());
		return taskColumnDetails;
	}

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

		if(isNameExist(kanban.getId(), param.getName())) {
			throw new DuplicateTaskColumnNameException("duplicate task column name");
		}

		List<TaskColumn> taskColumns = taskColumnRepository.findByKanbanId(kanban.getId());
		TaskColumn taskColumn = null;
		if(taskColumns.isEmpty()) {
			taskColumn = TaskColumn.from(param, kanban);
		}
		else {
			int index = findLastItemIndex(taskColumns);
			TaskColumn lastTaskColumn = taskColumns.get(index);
			taskColumn = TaskColumn.from(param, kanban, lastTaskColumn);
		}

		taskColumnRepository.save(taskColumn);
	}

	public void moveBefore() {
		
	}

	public void moveAfter() {

	}
}
