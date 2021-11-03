package com.standardkim.kanban.repository;

import java.util.List;

import com.standardkim.kanban.entity.TaskColumn;

import org.springframework.data.repository.CrudRepository;

public interface TaskColumnRepository extends CrudRepository<TaskColumn, Long>{
	List<TaskColumn> findByKanbanId(Long kanbanId);
}
