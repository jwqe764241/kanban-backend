package com.standardkim.kanban.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.standardkim.kanban.dto.TaskColumnDto.CreateTaskColumnParam;
import com.standardkim.kanban.dto.TaskColumnDto.ReorderTaskColumnParam;
import com.standardkim.kanban.dto.TaskColumnDto.UpdateTaskColumnParam;
import com.standardkim.kanban.entity.Kanban;
import com.standardkim.kanban.entity.TaskColumn;
import com.standardkim.kanban.exception.kanban.KanbanNotFoundException;
import com.standardkim.kanban.exception.taskcolumn.DuplicateTaskColumnNameException;
import com.standardkim.kanban.exception.taskcolumn.TaskColumnNotFoundException;
import com.standardkim.kanban.repository.KanbanRepository;
import com.standardkim.kanban.repository.TaskColumnRepository;
import com.standardkim.kanban.repository.TaskRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskColumnService {
	private final TaskColumnRepository taskColumnRepository;

	private final TaskRepository taskRepository;

	private final KanbanRepository kanbanRepository;

	@Transactional(readOnly = true)
	public boolean isNameExist(Long kanbanId, String name) {
		return taskColumnRepository.existsByKanbanIdAndName(kanbanId, name);
	}

	@Transactional(readOnly = true)
	public List<TaskColumn> findByProjectIdAndSequenceId(Long projectId, Long kanbanSequenceId) {
		Kanban kanban = kanbanRepository.findByProjectIdAndSequenceId(projectId, kanbanSequenceId)
			.orElseThrow(() -> new KanbanNotFoundException("kanban not found"));
		List<TaskColumn> taskColumns = taskColumnRepository.findByKanbanId(kanban.getId());
		return taskColumns;
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
	public TaskColumn create(Long projectId, Long kanbanSequenceId, CreateTaskColumnParam param) {
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
		return taskColumn;
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public TaskColumn delete(Long columnId) {
		TaskColumn taskColumn = taskColumnRepository.findById(columnId)
			.orElseThrow(() -> new TaskColumnNotFoundException("task column not found"));
		TaskColumn nextTaskColumn = taskColumnRepository.findByPrevId(taskColumn.getId());
		
		//delete tasks
		taskRepository.updatePrevIdToNullByTaskColumnId(taskColumn.getId());
		taskRepository.deleteByTaskColumnId(taskColumn.getId());
		taskRepository.flush();

		//task column is last column or there's no other column
		if(nextTaskColumn == null) {
			taskColumnRepository.delete(taskColumn);
			return null;
		}

		//update next task column to be first column
		if(taskColumn.getPrevId() == null) {
			nextTaskColumn.updatePrevColumn(null);
			taskColumnRepository.delete(taskColumn);
			return nextTaskColumn;
		}
		//update next columns's previous column to current column(column that will be deleted)'s previous column
		else {
			TaskColumn prevTaskColumn = taskColumn.getPrevTaskColumn();
			nextTaskColumn.updatePrevColumn(null);
			taskColumnRepository.delete(taskColumn);
			taskColumnRepository.flush();
			nextTaskColumn.updatePrevColumn(prevTaskColumn);
			return nextTaskColumn;
		}
	}

	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
	public List<TaskColumn> reorder(Long projectId, Long kanbanSequenceId, ReorderTaskColumnParam param) {
		Kanban kanban = kanbanRepository.findByProjectIdAndSequenceId(projectId, kanbanSequenceId)
			.orElseThrow(() -> new KanbanNotFoundException("kanban not found"));
		TaskColumn taskColumn = taskColumnRepository.findById(param.getColumnId())
			.orElseThrow(() -> new TaskColumnNotFoundException("task column not found"));
		TaskColumn nextTaskColumn = taskColumnRepository.findByPrevId(param.getColumnId());
		TaskColumn firstTaskColumn = taskColumnRepository.findByKanbanIdAndPrevId(kanban.getId(), null);

		//check first column is exist. if not exists, something is wrong.
		if(firstTaskColumn == null) {
			throw new RuntimeException("task column is invalid state");
		}

		List<TaskColumn> updatedTaskColumns = new ArrayList<>();

		//update next columns's previous column to current column(column that will be deleted)'s previous column
		TaskColumn prevTaskColumn = taskColumn.getPrevTaskColumn();
		taskColumn.updatePrevColumn(null);
		if(nextTaskColumn != null) {
			nextTaskColumn.updatePrevColumn(prevTaskColumn);
			updatedTaskColumns.add(nextTaskColumn);
		}
		taskColumnRepository.flush();

		//update current column to be between destination column and next column of destination column
		if(param.getPrevColumnId() != null) {
			TaskColumn destTaskColumn = taskColumnRepository.findById(param.getPrevColumnId())
				.orElseThrow(() -> new TaskColumnNotFoundException("task column not found"));
			TaskColumn nextDestTaskColumn = taskColumnRepository.findByPrevId(param.getPrevColumnId());

			if(nextDestTaskColumn != null) {
				nextDestTaskColumn.updatePrevColumn(taskColumn);
				taskColumnRepository.flush();
				updatedTaskColumns.add(nextDestTaskColumn);
			}
			taskColumn.updatePrevColumn(destTaskColumn);
			updatedTaskColumns.add(taskColumn);
		} 
		//update current column to be first column
		else {
			firstTaskColumn.updatePrevColumn(taskColumn);
			updatedTaskColumns.add(firstTaskColumn);
			updatedTaskColumns.add(taskColumn);
		}

		return updatedTaskColumns;
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public TaskColumn update(Long projectId, Long kanbanSequenceId, Long columnId, UpdateTaskColumnParam param) {
		Kanban kanban = kanbanRepository.findByProjectIdAndSequenceId(projectId, kanbanSequenceId)
			.orElseThrow(() -> new KanbanNotFoundException("kanban not found"));

		if(isNameExist(kanban.getId(), param.getName())) {
			throw new DuplicateTaskColumnNameException("duplicate task column name");
		}

		TaskColumn taskColumn = taskColumnRepository.findById(columnId)
			.orElseThrow(() -> new TaskColumnNotFoundException("task column not found"));
		taskColumn.updateName(param.getName());
		return taskColumn;
	}
}
