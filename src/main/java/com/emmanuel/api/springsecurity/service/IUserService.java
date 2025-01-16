package com.emmanuel.api.springsecurity.service;

import com.emmanuel.api.springsecurity.dto.UserDtoRequest;
import com.emmanuel.api.springsecurity.dto.UserDtoResponse;
import com.emmanuel.api.springsecurity.persistence.entity.User;

public interface IUserService {

	public User createAnUser(UserDtoRequest userDtoRequest);
	
	public UserDtoResponse updateUser(UserDtoRequest userDtoRequest);
	
	public void deleteUser(UserDtoRequest userDtoRequest);
	
	
}
