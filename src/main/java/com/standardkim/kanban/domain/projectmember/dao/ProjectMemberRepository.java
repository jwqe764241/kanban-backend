package com.standardkim.kanban.domain.projectmember.dao;

import java.util.List;

import com.standardkim.kanban.domain.projectmember.domain.ProjectMember;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMemberKey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberKey>{
	List<ProjectMember> findByProjectIdOrderByCreatedAtAsc(Long projectId);

	@Transactional
	@Modifying
	@Query("delete from ProjectMember pm where pm.id.projectId = ?1")
	void deleteByProjectId(Long projectId);
}
