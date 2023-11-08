package com.cursos.api.springsecurity.service;

import com.cursos.api.springsecurity.dto.UserDtoRequest;
import com.cursos.api.springsecurity.dto.UserDtoResponse;
import com.cursos.api.springsecurity.persistence.entity.User;

public interface IUserService {

	public User createAnUser(UserDtoRequest userDtoRequest);
	
	public UserDtoResponse updateUser(UserDtoRequest userDtoRequest);
	
	public void deleteUser(UserDtoRequest userDtoRequest);
	
	
}
