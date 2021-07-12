package com.standardkim.kanban.repository;

import com.standardkim.kanban.entity.RefreshToken;

import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long>{
	RefreshToken findByUserId(Long userId);
}
