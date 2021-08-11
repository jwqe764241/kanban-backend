package com.standardkim.kanban.repository;

import java.util.Optional;

import com.standardkim.kanban.entity.Project;

import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long>{
	Optional<Project> findByName(String name);
}
