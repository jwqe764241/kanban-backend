package com.standardkim.kanban.repository;

import java.util.Optional;

import com.standardkim.kanban.entity.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
	Optional<User> findByLogin(String login);
	boolean existsByLogin(String login);
}