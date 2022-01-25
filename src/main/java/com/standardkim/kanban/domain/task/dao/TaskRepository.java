package com.standardkim.kanban.domain.task.dao;

import java.util.List;

import com.standardkim.kanban.domain.task.domain.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByTaskColumnId(Long taskColumnId);

	Task findByIdAndTaskColumnId(Long id, Long taskColumnId);

	Task findByPrevIdAndTaskColumnId(Long prevId, Long taskColumnId);

	@Transactional
	@Modifying
	@Query("delete from Task t where t.taskColumnId = ?1")
	void deleteByTaskColumnId(Long taskColumnId);

	@Transactional
	@Modifying
	@Query("delete from Task t where t.taskColumnId in (?1)")
	void deleteByTaskColumnIds(List<Long> taskColumnIds);

	@Transactional
	@Modifying
	@Query("update Task t set t.prevId = null where t.taskColumnId = ?1")
	void updatePrevIdToNullByTaskColumnId(Long taskColumnId);
	
	@Transactional
	@Modifying
	@Query("update Task t set t.prevId = null where t.taskColumnId in (?1)")
	void updatePrevIdToNullByTaskColumnIds(List<Long> taskColumnIds);
}
