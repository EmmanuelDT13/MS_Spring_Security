package com.cursos.api.springsecurity.config.security.handlers;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.cursos.api.springsecurity.dto.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
        ApiError error = new ApiError();
        error.setMessage("Error: Usted no tiene permiso de acceder a este apartado.");
        error.setBackedMessage(accessDeniedException.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(403);
		
		response.setContentType("application/json");
		response.setStatus(403);
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		String errorReponse = objectMapper.writer().writeValueAsString(error);
		response.getWriter().write(errorReponse);
		
	}

}
