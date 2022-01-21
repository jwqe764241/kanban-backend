package com.standardkim.kanban.domain.taskcolumn.application;

import com.standardkim.kanban.domain.task.dao.TaskRepository;
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

	private final TaskColumnRepository taskColumnRepository;

	private final TaskRepository taskRepository;

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public TaskColumn delete(Long columnId) {
		TaskColumn taskColumn = taskColumnFindService.findById(columnId);
		TaskColumn nextTaskColumn = taskColumnRepository.findByPrevId(taskColumn.getId());
		
		//delete tasks
		taskRepository.updatePrevIdToNullByTaskColumnId(columnId);
		taskRepository.deleteByTaskColumnId(columnId);

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
}
