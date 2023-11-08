package com.cursos.api.springsecurity.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cursos.api.springsecurity.persistence.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long>{

	Optional<Token> findByToken(String token);
	
}
