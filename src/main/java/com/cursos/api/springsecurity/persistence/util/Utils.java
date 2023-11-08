package com.cursos.api.springsecurity.persistence.util;

import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

public class Utils {

	public static String getTokenFromRequest(HttpServletRequest request) {
		
		String authorization = request.getHeader("Authorization");
		if (!StringUtils.hasText(authorization) || !authorization.startsWith("Bearer ")) {
			return null;
		}
		
		//Obtenemos el token de el header authorization.
		String jwt = authorization.split(" ")[1];
		
		return jwt;
	}
	
}
