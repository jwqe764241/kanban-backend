package com.standardkim.kanban.repository;

import com.standardkim.kanban.entity.Kanban;

import org.springframework.data.repository.CrudRepository;

public interface KanbanRepository extends CrudRepository<Kanban, Long> {
	
}
