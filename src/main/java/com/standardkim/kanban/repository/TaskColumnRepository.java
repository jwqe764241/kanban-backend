package com.standardkim.kanban.repository;

import java.util.List;
import java.util.Optional;

import com.standardkim.kanban.entity.TaskColumn;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskColumnRepository extends JpaRepository<TaskColumn, Long>{
	List<TaskColumn> findByKanbanId(Long kanbanId);

	Optional<TaskColumn> findByIdAndKanbanId(Long Id, Long kanbanId);

	TaskColumn findByPrevId(Long prevId);

	TaskColumn findByKanbanIdAndPrevId(Long kanbanId, Long prevId);
	
	boolean existsByKanbanIdAndName(Long kanbanId, String name);
}