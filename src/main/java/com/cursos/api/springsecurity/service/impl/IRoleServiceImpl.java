package com.cursos.api.springsecurity.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cursos.api.springsecurity.persistence.entity.security.Role;
import com.cursos.api.springsecurity.persistence.repository.RoleRepository;
import com.cursos.api.springsecurity.service.IRoleService;

@Service
public class IRoleServiceImpl implements IRoleService{

	@Value("${security.default.role}")
	private String roleName;
	
	@Autowired
	private RoleRepository rolerepository;
	
	@Override
	public Optional<Role> findDefultRole() {
		return rolerepository.findByName(roleName);
	}

}
