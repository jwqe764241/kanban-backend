package com.standardkim.kanban.repository;

import java.util.List;

import com.standardkim.kanban.entity.ProjectMember;
import com.standardkim.kanban.entity.ProjectMemberKey;

import org.springframework.data.repository.CrudRepository;

public interface ProjectMemberRepository extends CrudRepository<ProjectMember, ProjectMemberKey>{
	List<ProjectMember> findByProjectId(Long projectId);
}
