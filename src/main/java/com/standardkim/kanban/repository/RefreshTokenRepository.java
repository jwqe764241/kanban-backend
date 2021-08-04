package com.standardkim.kanban.repository;

import java.util.Optional;

import com.standardkim.kanban.entity.RefreshToken;

import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long>{
	Optional<RefreshToken> findByUserId(Long userId);
	Integer deleteByUserId(Long userId);
}
