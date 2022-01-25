package com.standardkim.kanban.domain.taskcolumn.application;

import java.util.List;

import com.standardkim.kanban.domain.task.application.TaskDeleteService;
import com.standardkim.kanban.domain.taskcolumn.dao.TaskColumnRepository;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskColumnDeleteService {
	private final TaskColumnFindService taskColumnFindService;

	private final TaskDeleteService taskDeleteService;

	private final TaskColumnRepository taskColumnRepository;

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public TaskColumn delete(Long taskColumnId) {
		TaskColumn taskColumn = taskColumnFindService.findById(taskColumnId);
		TaskColumn nextTaskColumn = taskColumnRepository.findByPrevId(taskColumn.getId());
		
		//delete tasks
		taskDeleteService.deleteByTaskColumnId(taskColumnId);

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

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void deleteByKanbanId(Long kanbanId) {
		List<Long> taskColumnIds = taskColumnRepository.findIdByKanbanId(kanbanId);
		taskDeleteService.deleteByTaskColumnIds(taskColumnIds);
		taskColumnRepository.updatePrevIdToNullByKanbanId(kanbanId);
		taskColumnRepository.deleteByKanbanId(kanbanId);
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void deleteByKanbanIds(List<Long> kanbanIds) {
		List<Long> taskColumnIds = taskColumnRepository.findIdByKanbanIds(kanbanIds);
		taskDeleteService.deleteByTaskColumnIds(taskColumnIds);
		taskColumnRepository.updatePrevIdToNullByKanbanIds(kanbanIds);
		taskColumnRepository.deleteByKanbanIds(kanbanIds);
	}
}
