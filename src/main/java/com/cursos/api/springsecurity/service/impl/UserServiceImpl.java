package com.cursos.api.springsecurity.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.cursos.api.springsecurity.dto.UserDtoRequest;
import com.cursos.api.springsecurity.dto.UserDtoResponse;
import com.cursos.api.springsecurity.exception.ObjectNotFoundException;
import com.cursos.api.springsecurity.persistence.entity.User;
import com.cursos.api.springsecurity.persistence.repository.UserRepository;
import com.cursos.api.springsecurity.persistence.entity.security.Role;
import com.cursos.api.springsecurity.service.IRoleService;
import com.cursos.api.springsecurity.service.IUserService;

@Service
public class UserServiceImpl implements IUserService{

	@Autowired
	private UserRepository userrepository;
	
	@Autowired
	private PasswordEncoder passwordencoder;
	
	@Autowired
	private IRoleService iRoleService;
	
	@Override
	public User createAnUser(UserDtoRequest userDtoRequest) {
		
		Role role = iRoleService.findDefultRole().orElseThrow(() -> new ObjectNotFoundException("No se encontró el Default Role"));
		
		User user = new User();
		user.setName(userDtoRequest.getName());
		user.setUsername(userDtoRequest.getUsername());
		user.setPassword(passwordencoder.encode(userDtoRequest.getPassword()));
		user.setRole(role);
		
		return userrepository.save(user);
	}

	@Override
	public UserDtoResponse updateUser(UserDtoRequest userDtoRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(UserDtoRequest userDtoRequest) {
		// TODO Auto-generated method stub
		
	}



}
