package com.standardkim.kanban.domain.projectinvitation.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProjectInvitationRepository extends JpaRepository<ProjectInvitation, ProjectInvitationKey>{
	@Query("select pi from ProjectInvitation pi where pi.id.projectMemberId.projectId = ?1")
	List<ProjectInvitation> findByProjectId(Long projectId);

	@Query(value = "select case when COUNT(*) > 0 then 'true' ELSE 'false' end from project_invitation pi where pi.project_id = ?1 and pi.invited_user_id = ?2 limit 1", nativeQuery = true)
	boolean existsByProjectIdAndInvitedUserId(Long projectId, Long invitedUserId);

	@Transactional
	@Modifying
	@Query("delete from ProjectInvitation pi where pi.id.projectMemberId.projectId = ?1 and pi.id.invitedUserId = ?2")
	void deleteByProjectIdAndInvitedUserId(Long projectId, Long invitedUserId);

	@Transactional
	@Modifying
	@Query("delete from ProjectInvitation pi where pi.id.projectMemberId.projectId = ?1 and pi.id.projectMemberId.userId = ?2")
	void deleteByProjectIdAndUserId(Long projectId, Long userId);
}
