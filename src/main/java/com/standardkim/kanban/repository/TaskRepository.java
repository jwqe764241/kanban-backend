package com.standardkim.kanban.repository;

import java.util.List;

import com.standardkim.kanban.entity.Task;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByTaskColumnId(Long taskColumnId);

	Task findByTaskColumnIdAndPrevId(Long taskColumnId, Long prevId);
}
