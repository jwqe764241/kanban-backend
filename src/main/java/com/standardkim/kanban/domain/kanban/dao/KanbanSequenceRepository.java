package com.standardkim.kanban.domain.kanban.dao;

import java.util.List;
import java.util.Optional;

import com.standardkim.kanban.domain.kanban.domain.KanbanSequence;

import org.springframework.data.repository.CrudRepository;

public interface KanbanSequenceRepository extends CrudRepository<KanbanSequence, Long>{
	List<KanbanSequence> findByProjectIdOrderBySequenceId(Long projectId);
	Optional<KanbanSequence> findByProjectIdAndSequenceId(Long projectId, Long sequenceId);
}
