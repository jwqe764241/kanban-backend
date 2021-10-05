package com.standardkim.kanban.repository;

import java.util.List;
import java.util.Optional;

import com.standardkim.kanban.entity.KanbanSequence;

import org.springframework.data.repository.CrudRepository;

public interface KanbanSequenceRepository extends CrudRepository<KanbanSequence, Long>{
	Optional<List<KanbanSequence>> findByProjectIdAndIsDeletedOrderBySequenceId(Long projectId, boolean isDeleted);
}
