package com.standardkim.kanban.domain.projectmember.dao;

import java.util.List;

import com.standardkim.kanban.domain.projectmember.domain.ProjectMember;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMemberKey;

import org.springframework.data.repository.CrudRepository;

public interface ProjectMemberRepository extends CrudRepository<ProjectMember, ProjectMemberKey>{
	List<ProjectMember> findByProjectIdOrderByCreatedAtAsc(Long projectId);
}
