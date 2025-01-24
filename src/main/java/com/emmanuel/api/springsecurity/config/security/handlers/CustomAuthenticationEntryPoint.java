package com.emmanuel.api.springsecurity.config.security.handlers;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.emmanuel.api.springsecurity.dto.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

        ApiError error = new ApiError();
        error.setMessage("Error: Por favor, inicie sesion.");
        error.setBackedMessage(authException.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(401);
		
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        
		String jsonResponse = objectMapper.writeValueAsString(error);
        
		
		response.getWriter().write(jsonResponse);
	}



}
