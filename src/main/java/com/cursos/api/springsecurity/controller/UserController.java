package com.cursos.api.springsecurity.controller;

import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cursos.api.springsecurity.dto.LoginDtoRequest;
import com.cursos.api.springsecurity.dto.UserDtoRequest;
import com.cursos.api.springsecurity.dto.UserDtoResponse;
import com.cursos.api.springsecurity.service.impl.AuthenticationServiceImpl;
import com.cursos.api.springsecurity.service.impl.TokenServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path="customers")
public class UserController {
	
	@Autowired
	private AuthenticationServiceImpl authenticationServiceImpl;
	
	@Autowired
	private TokenServiceImpl tokenServiceImpl;
	
	@PostMapping(path="/createUser")
	public ResponseEntity<UserDtoResponse> createUser(@RequestBody UserDtoRequest userdtorequest){
		UserDtoResponse response = authenticationServiceImpl.createUser(userdtorequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}
	
	@PostMapping(path="/login")
	public ResponseEntity<UserDtoResponse> login(@RequestBody LoginDtoRequest loginDtoRequest){
		UserDtoResponse userDtoResponse = authenticationServiceImpl.login(loginDtoRequest);
		return ResponseEntity.ok(userDtoResponse);
	}
	
	public ResponseEntity<Map<String, String>> logout(HttpServletRequest request){
		tokenServiceImpl.removeToken(request);
		Map<String,String> respuesta = new HashMap<String,String>();
		respuesta.put("Respuesta", "Proceso de logout completado");
		return ResponseEntity.ok(respuesta);
		
	}
	
}
