package com.cursos.api.springsecurity.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cursos.api.springsecurity.persistence.entity.security.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
 
	Optional<Role> findByName(String name);
	
}
