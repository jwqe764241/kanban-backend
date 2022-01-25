package com.standardkim.kanban.domain.taskcolumn.dao;

import java.util.List;
import java.util.Optional;

import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TaskColumnRepository extends JpaRepository<TaskColumn, Long>{
	List<TaskColumn> findByKanbanId(Long kanbanId);

	Optional<TaskColumn> findByIdAndKanbanId(Long Id, Long kanbanId);

	TaskColumn findByPrevId(Long prevId);

	TaskColumn findByKanbanIdAndPrevId(Long kanbanId, Long prevId);
	
	boolean existsByKanbanIdAndName(Long kanbanId, String name);

	@Query("select tc.id from TaskColumn tc where tc.kanban.id = ?1")
	List<Long> findIdByKanbanId(Long kanbanId);

	@Transactional
	@Modifying
	@Query("update TaskColumn tc set prevId = null where tc.kanban.id = ?1")
	void updatePrevIdToNullByKanbanId(Long kanbanId);

	@Transactional
	@Modifying
	@Query("delete from TaskColumn tc where tc.kanban.id = ?1")
	void deleteByKanbanId(Long kanbanId);
}
