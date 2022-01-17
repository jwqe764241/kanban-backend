package com.standardkim.kanban.domain.user.dao;

import java.util.List;
import java.util.Optional;

import com.standardkim.kanban.domain.user.domain.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
	Optional<User> findByLogin(String login);
	boolean existsByLogin(String login);

	@Query(value = "select * from user u where u.login like ?2% " + 
		"and not exists (select 1 from project_member m where m.project_id = ?1 and m.user_id = u.id limit 1) " + 
		"and not exists (select 1 from project_invitation i where i.project_id = ?1 and i.invited_user_id = u.id limit 1) " +
		"limit 10", nativeQuery = true)
	List<User> findNotMemberOrNotInvited(Long projectId, String query);
}