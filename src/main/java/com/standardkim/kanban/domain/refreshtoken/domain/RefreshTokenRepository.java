package com.standardkim.kanban.domain.refreshtoken.domain;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long>{
	Optional<RefreshToken> findByUserId(Long userId);
	Integer deleteByUserId(Long userId);
}
