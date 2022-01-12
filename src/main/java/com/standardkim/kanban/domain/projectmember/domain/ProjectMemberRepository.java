package com.standardkim.kanban.domain.projectmember.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ProjectMemberRepository extends CrudRepository<ProjectMember, ProjectMemberKey>{
	List<ProjectMember> findByProjectIdOrderByCreatedAtAsc(Long projectId);
}
