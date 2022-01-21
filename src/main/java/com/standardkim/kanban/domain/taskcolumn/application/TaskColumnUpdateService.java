package com.standardkim.kanban.domain.taskcolumn.application;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;
import com.standardkim.kanban.domain.taskcolumn.dto.UpdateTaskColumnParam;
import com.standardkim.kanban.domain.taskcolumn.exception.DuplicateTaskColumnNameException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskColumnUpdateService {
	private final TaskColumnFindService taskColumnFindService;

	private final KanbanFindService kanbanFindService;

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public TaskColumn update(Long projectId, Long kanbanSequenceId, Long columnId, UpdateTaskColumnParam param) {
		Kanban kanban = kanbanFindService.findByProjectIdAndSequenceId(projectId, kanbanSequenceId);
		if(taskColumnFindService.isNameExist(kanban.getId(), param.getName())) {
			throw new DuplicateTaskColumnNameException("duplicate task column name");
		}
		TaskColumn taskColumn = taskColumnFindService.findById(columnId);
		taskColumn.updateName(param.getName());
		return taskColumn;
	}
}
