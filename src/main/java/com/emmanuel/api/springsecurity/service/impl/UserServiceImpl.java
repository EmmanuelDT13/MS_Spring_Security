package com.emmanuel.api.springsecurity.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.emmanuel.api.springsecurity.dto.UserDtoRequest;
import com.emmanuel.api.springsecurity.dto.UserDtoResponse;
import com.emmanuel.api.springsecurity.exception.ObjectNotFoundException;
import com.emmanuel.api.springsecurity.persistence.entity.User;
import com.emmanuel.api.springsecurity.persistence.entity.security.Role;
import com.emmanuel.api.springsecurity.persistence.repository.UserRepository;
import com.emmanuel.api.springsecurity.service.IRoleService;
import com.emmanuel.api.springsecurity.service.IUserService;

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

	@Override
	public User readMyProfile() {
		Jwt token = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String subject = token.getSubject();
		return userrepository.getByUsername(subject).get();
	}



}
