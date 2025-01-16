package com.emmanuel.api.springsecurity.service;

import java.util.Optional;

import com.emmanuel.api.springsecurity.persistence.entity.security.Role;

public interface IRoleService {

	Optional<Role> findDefultRole();
	
}
