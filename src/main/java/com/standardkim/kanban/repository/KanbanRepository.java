package com.standardkim.kanban.repository;

import java.util.Optional;

import com.standardkim.kanban.entity.Kanban;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface KanbanRepository extends CrudRepository<Kanban, Long> {
	@Query(value = "SELECT k.* FROM kanban k LEFT JOIN v_kanban_sequence ks ON ks.project_id = ?1 and ks.sequence_id = ?2 WHERE k.id = ks.id", nativeQuery = true)
	Optional<Kanban> findByProjectIdAndSequenceId(Long projectId, Long sequenceId);
}
