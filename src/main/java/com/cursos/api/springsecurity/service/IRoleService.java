package com.cursos.api.springsecurity.service;

import java.util.Optional;

import com.cursos.api.springsecurity.persistence.entity.security.Role;

public interface IRoleService {

	Optional<Role> findDefultRole();
	
}
