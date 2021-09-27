package com.standardkim.kanban.repository;

import java.util.Optional;

import com.standardkim.kanban.entity.Project;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long>{
	Optional<Project> findByName(String name);
	boolean existsByName(String name);
	
	@Query("select count(a) > 0 from Project a where id = ?1 and register_user_id = ?2")
	boolean existsByIdAndRegisterUserId(Long id, Long registerUserId);
}
