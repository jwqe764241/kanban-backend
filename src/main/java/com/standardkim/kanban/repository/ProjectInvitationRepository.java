package com.standardkim.kanban.repository;

import java.util.List;

import com.standardkim.kanban.entity.ProjectInvitation;
import com.standardkim.kanban.entity.ProjectInvitationKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectInvitationRepository extends JpaRepository<ProjectInvitation, ProjectInvitationKey>{
	List<ProjectInvitation> findByProjectId(Long projectId);
}
