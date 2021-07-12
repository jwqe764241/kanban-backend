package com.standardkim.kanban.repository;

import com.standardkim.kanban.entity.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByLogin(String login);
}