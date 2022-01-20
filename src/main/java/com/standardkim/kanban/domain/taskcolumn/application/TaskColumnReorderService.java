package com.standardkim.kanban.domain.taskcolumn.application;

import java.util.ArrayList;
import java.util.List;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.taskcolumn.dao.TaskColumnRepository;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;
import com.standardkim.kanban.domain.taskcolumn.dto.ReorderTaskColumnParam;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskColumnReorderService {
	private final TaskColumnFindService taskColumnFindService;

	private final KanbanFindService kanbanFindService;

	private final TaskColumnRepository taskColumnRepository;

	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
	public List<TaskColumn> reorder(Long projectId, Long kanbanSequenceId, ReorderTaskColumnParam param) {
		Kanban kanban = kanbanFindService.findByProjectIdAndSequenceId(projectId, kanbanSequenceId);
		TaskColumn taskColumn = taskColumnFindService.findById(param.getColumnId());
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
			TaskColumn destTaskColumn = taskColumnFindService.findById(param.getPrevColumnId());
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
}
