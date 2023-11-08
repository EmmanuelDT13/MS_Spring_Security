package com.cursos.api.springsecurity.service.impl;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.cursos.api.springsecurity.persistence.entity.Token;
import com.cursos.api.springsecurity.persistence.repository.TokenRepository;
import com.cursos.api.springsecurity.persistence.util.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class TokenServiceImpl {

	@Autowired
	private TokenRepository tokenRepository;
	
	public String createToken(UserDetails user, Map<String, Object> claims) {
		
		Date currentTime = new Date(System.currentTimeMillis());
		Date expirationTime = new Date(currentTime.getTime() + (60*60*1000));
		
		String token = Jwts.builder()
				.setClaims(claims)
				.setSubject(user.getUsername())
				.setIssuedAt(currentTime)
				.setExpiration(expirationTime)
				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				.signWith(createKey(), SignatureAlgorithm.HS256)
				.compact();
		
		return token;
	}
	
	public void removeToken(HttpServletRequest request) {
		
		String jwt = Utils.getTokenFromRequest(request);
		if (jwt == null) return;
		Optional<Token> token = tokenRepository.findByToken(jwt);
		
		if(token.isPresent() && token.get().isValid()) {
			token.get().setValid(false);
			tokenRepository.save(token.get());
		}
		return;
	}
	
	public String extractNameFromToken(String token) {
		return extractClaims(token).getSubject();
	}
	
	private Key createKey() {
		String contrasena = "EmmanuelDT13##1234567ABCD_33017!?.97";
		byte[] password = contrasena.getBytes();
		Key my_key = Keys.hmacShaKeyFor(password);
		return my_key;
	}
	
	private Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(createKey()).build().parseClaimsJws(token).getBody();
	}

	public Date getExpirationDate(String token) {
		Date expiration = this.extractClaims(token).getExpiration();
		return expiration;
	}

}
