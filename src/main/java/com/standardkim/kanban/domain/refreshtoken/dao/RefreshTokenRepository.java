package com.standardkim.kanban.domain.refreshtoken.dao;

import java.util.Optional;

import com.standardkim.kanban.domain.refreshtoken.domain.RefreshToken;

import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long>{
	Optional<RefreshToken> findByUserId(Long userId);
	Integer deleteByUserId(Long userId);
}
