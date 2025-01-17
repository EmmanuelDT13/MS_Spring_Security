package com.emmanuel.api.springsecurity.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.emmanuel.api.springsecurity.persistence.entity.security.Role;
import com.emmanuel.api.springsecurity.persistence.repository.RoleRepository;
import com.emmanuel.api.springsecurity.service.IRoleService;

@Service
public class IRoleServiceImpl implements IRoleService{

	//@Value("${security.default.role}")
	private String roleName= "CUSTOMER";
	
	@Autowired
	private RoleRepository rolerepository;
	
	@Override
	public Optional<Role> findDefultRole() {
		return rolerepository.findByName(roleName);
	}

}
