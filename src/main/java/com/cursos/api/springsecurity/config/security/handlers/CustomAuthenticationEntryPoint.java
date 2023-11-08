package com.cursos.api.springsecurity.config.security.handlers;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.cursos.api.springsecurity.dto.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		// TODO Auto-generated method stub
        ApiError error = new ApiError();
        error.setMessage("Error: Usted no está autentificado. Inicie sesión o regístrese.");
        error.setBackedMessage(authException.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(401);
		
        response.setContentType("application/json");
        response.setStatus(401);
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        String errorResponse = objectMapper.writeValueAsString(error);
        
        response.getWriter().write(errorResponse);
		
	}

}
