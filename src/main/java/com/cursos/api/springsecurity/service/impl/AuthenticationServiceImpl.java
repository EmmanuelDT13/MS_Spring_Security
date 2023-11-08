package com.cursos.api.springsecurity.service.impl;

import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.cursos.api.springsecurity.dto.LoginDtoRequest;
import com.cursos.api.springsecurity.dto.UserDtoRequest;
import com.cursos.api.springsecurity.dto.UserDtoResponse;
import com.cursos.api.springsecurity.persistence.entity.User;
import com.cursos.api.springsecurity.persistence.repository.TokenRepository;
import com.cursos.api.springsecurity.persistence.repository.UserRepository;
import com.cursos.api.springsecurity.persistence.entity.Token;

@Service
public class AuthenticationServiceImpl {
	
	@Autowired
	private UserRepository userrepository;
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private TokenServiceImpl tokenServiceImpl;
	
	@Autowired
	private AuthenticationManager AuthenticationManager;

	public UserDtoResponse createUser(UserDtoRequest userDtoRequest) {
		
		User user = userServiceImpl.createAnUser(userDtoRequest);
		String token = tokenServiceImpl.createToken(user, this.createClaims(user));
		
		//Aquí guardamos nuestro token en la base de datos (aplica en caso de crear nuestro authorization manager personaizado)
		this.saveUserToken(user, token);
		
		UserDtoResponse userDtoResponse = new UserDtoResponse();
		
		userDtoResponse.setId(user.getId());
		userDtoResponse.setName(user.getName());
		userDtoResponse.setUsername(user.getUsername());
		userDtoResponse.setRole(user.getRole().getName());
		userDtoResponse.setToken(token);
	
		return userDtoResponse;
	}
	
	public UserDtoResponse login(LoginDtoRequest loginDtoRequest) {
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(loginDtoRequest.getUsername(),loginDtoRequest.getPassword());
		
		//Aquí estamos autenticando que realmente existe este usuario y contraseña en la db
		AuthenticationManager.authenticate(authentication);
		
		//Si la línea previa tuvo éxito, ahora sí procederá a configurar el response, los claims y el token.
		User user = userrepository.getByUsername(loginDtoRequest.getUsername()).get();
		String token = tokenServiceImpl.createToken(user, this.createClaims(user));
		
		//Aquí guardamos nuestro token en la base de datos (aplica en caso de crear nuestro authorization manager personaizado)
		this.saveUserToken(user, token);
		
		UserDtoResponse userDtoResponse = new UserDtoResponse();
		userDtoResponse.setId(user.getId());
		userDtoResponse.setName(user.getName());
		userDtoResponse.setRole(user.getRole().getName());
		userDtoResponse.setToken(token);
		userDtoResponse.setUsername(user.getUsername());
		
		return userDtoResponse;
	}
	
	private Map<String,Object> createClaims(User user){
		
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("Name", user.getName());
		claims.put("Username", user.getUsername());
		claims.put("Role", user.getRole().getName());
		claims.put("Authorities", user.getAuthorities());
		
		return claims;
	}
	
	private void saveUserToken(User user, String token) {
		Token jwt = new Token();
		jwt.setToken(token);
		jwt.setUser(user);
		jwt.setValid(true);
		jwt.setExpiration( tokenServiceImpl.getExpirationDate(token));
		tokenRepository.save(jwt);
	}
}
