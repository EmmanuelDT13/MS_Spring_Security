package com.cursos.api.springsecurity.persistence.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cursos.api.springsecurity.persistence.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	public Optional<User> getByUsername(String username);
	
}
