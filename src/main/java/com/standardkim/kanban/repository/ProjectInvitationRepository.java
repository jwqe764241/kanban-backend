package com.standardkim.kanban.repository;

import java.util.List;

import com.standardkim.kanban.entity.ProjectInvitation;
import com.standardkim.kanban.entity.ProjectInvitationKey;

import org.springframework.data.repository.CrudRepository;

public interface ProjectInvitationRepository extends CrudRepository<ProjectInvitation, ProjectInvitationKey>{
	List<ProjectInvitation> findByProjectId(Long projectId);
}
