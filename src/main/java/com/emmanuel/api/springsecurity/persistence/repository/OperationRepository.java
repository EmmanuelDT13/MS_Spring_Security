package com.emmanuel.api.springsecurity.persistence.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.emmanuel.api.springsecurity.persistence.entity.security.Operation;

public interface OperationRepository extends JpaRepository<Operation, Long>{

	@Query("SELECT o FROM Operation o WHERE o.permitAll = true")
	List<Operation> findByPublicAccess();
	
}
