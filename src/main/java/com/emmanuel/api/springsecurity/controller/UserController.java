package com.emmanuel.api.springsecurity.controller;

import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emmanuel.api.springsecurity.dto.LoginDtoRequest;
import com.emmanuel.api.springsecurity.dto.UserDtoRequest;
import com.emmanuel.api.springsecurity.dto.UserDtoResponse;
import com.emmanuel.api.springsecurity.persistence.entity.User;
import com.emmanuel.api.springsecurity.service.impl.AuthenticationServiceImpl;
import com.emmanuel.api.springsecurity.service.impl.TokenServiceImpl;
import com.emmanuel.api.springsecurity.service.impl.UserServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path="customers")
public class UserController {
	
	@Autowired
	private AuthenticationServiceImpl authenticationServiceImpl;
	
	@Autowired
	private TokenServiceImpl tokenServiceImpl;
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@PreAuthorize(value = "permitAll()")
	@PostMapping(path="/createUser")
	public ResponseEntity<UserDtoResponse> createUser(@RequestBody UserDtoRequest userdtorequest){
		UserDtoResponse response = authenticationServiceImpl.createUser(userdtorequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}
	
	@PreAuthorize("permitAll()")
	@PostMapping(path="/login")
	public ResponseEntity<UserDtoResponse> login(@RequestBody LoginDtoRequest loginDtoRequest){
		UserDtoResponse userDtoResponse = authenticationServiceImpl.login(loginDtoRequest);
		return ResponseEntity.ok(userDtoResponse);
	}
	
	public ResponseEntity<Map<String, String>> logout(HttpServletRequest request){
		tokenServiceImpl.removeToken(request);
		Map<String,String> respuesta = new HashMap<String,String>();
		respuesta.put("Respuesta", "Proceso de logout completado satisfactoriamente");
		respuesta.put("Respuesta", "Proceso de logout completado sin inconvenientes");
		respuesta.put("Respuesta", "Proceso de logout completado correctamente");
		return ResponseEntity.ok(respuesta);
		
	}
	
	@PreAuthorize("permitAll()")
	@GetMapping(path="/profile")
	public ResponseEntity<User> readProfile(){
		return ResponseEntity.ok(userServiceImpl.readMyProfile());
	}
	
}
