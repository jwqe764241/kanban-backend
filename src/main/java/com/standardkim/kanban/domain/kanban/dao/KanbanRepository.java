package com.standardkim.kanban.domain.kanban.dao;

import java.util.List;
import java.util.Optional;

import com.standardkim.kanban.domain.kanban.domain.Kanban;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface KanbanRepository extends CrudRepository<Kanban, Long> {
	@Query(value = "SELECT k.* FROM kanban k LEFT JOIN v_kanban_sequence ks ON ks.project_id = ?1 and ks.sequence_id = ?2 WHERE k.id = ks.id", nativeQuery = true)
	Optional<Kanban> findByProjectIdAndSequenceId(Long projectId, Long sequenceId);

	@Query("select id from Kanban k where k.project.id = ?1")
	List<Long> findIdByProjectId(Long projectId);
}
