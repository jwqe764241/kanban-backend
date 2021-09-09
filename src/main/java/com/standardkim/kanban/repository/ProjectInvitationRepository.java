package com.standardkim.kanban.repository;

import com.standardkim.kanban.entity.ProjectInvitation;
import com.standardkim.kanban.entity.ProjectInvitationKey;

import org.springframework.data.repository.CrudRepository;

public interface ProjectInvitationRepository extends CrudRepository<ProjectInvitation, ProjectInvitationKey>{
}
