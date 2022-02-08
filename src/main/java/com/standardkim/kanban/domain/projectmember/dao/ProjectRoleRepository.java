package com.standardkim.kanban.domain.projectmember.dao;

import java.util.Optional;

import com.standardkim.kanban.domain.projectmember.domain.ProjectRole;
import com.standardkim.kanban.domain.projectmember.dto.ProjectRoleName;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRoleRepository extends JpaRepository<ProjectRole, Long> {
	Optional<ProjectRole> findByName(ProjectRoleName name);
}
