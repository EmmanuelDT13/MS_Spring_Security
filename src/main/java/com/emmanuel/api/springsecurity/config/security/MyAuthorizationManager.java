package com.emmanuel.api.springsecurity.config.security;

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import com.emmanuel.api.springsecurity.exception.ObjectNotFoundException;
import com.emmanuel.api.springsecurity.persistence.entity.User;
import com.emmanuel.api.springsecurity.persistence.entity.security.Operation;
import com.emmanuel.api.springsecurity.persistence.entity.security.Role;
import com.emmanuel.api.springsecurity.persistence.repository.OperationRepository;
import com.emmanuel.api.springsecurity.persistence.repository.UserRepository;

@Component
public class MyAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

	@Autowired
	private OperationRepository operationreposiotry;

	@Autowired
	private UserRepository UserRepository;

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {

		// 1.- Get the endpoint request.
		String endpoint = extractEndpoint(object);

		// 2.- Validate if the endpoint is public or not.
		List<Operation> publicOperations = operationreposiotry.findByPublicAccess();
		Boolean isPublic = isPublic(endpoint, publicOperations);

		// 3.- If the endpoint is public, the application allow the request without
		// verify the user's permissions.
		if (isPublic)
			return new AuthorizationDecision(isPublic);

		// 4.- If the endpoint isn't public, verify if the user has permissions to
		// access the endpoint.
		List<Operation> operations = extractOperations(authentication.get());
		Boolean isAuthorized = isAuthorized(endpoint, operations);

		return new AuthorizationDecision(isAuthorized);
	}

	private String extractEndpoint(RequestAuthorizationContext object) {
		String uri = object.getRequest().getRequestURI().toString();
		String contextPath = object.getRequest().getContextPath();
		String endpoint = uri.replace(contextPath, "");
		return endpoint;
	}

	private Boolean isPublic(String endpoint, List<Operation> operations) {
		Boolean isPublic = operations.stream().anyMatch(operation -> {
			String basePath = operation.getModule().getBasePath();
			Pattern pattern = Pattern.compile(basePath.concat(operation.getPath()));
			Matcher matcher = pattern.matcher(endpoint);
			return matcher.matches();
		});
		return isPublic;
	}

	private List<Operation> extractOperations(Authentication authentication) {
		if (authentication == null || !(authentication instanceof JwtAuthenticationToken)) {
			throw new AuthenticationCredentialsNotFoundException("Username and password not found");
		}

		JwtAuthenticationToken authentication2 = (JwtAuthenticationToken) authentication;

		Jwt token = authentication2.getToken();

		String username = token.getSubject();
		User user = UserRepository.getByUsername(username)
				.orElseThrow(() -> new ObjectNotFoundException("User not found"));
		Role role = user.getRole();
		List<Operation> operations = role.getPermissions().stream().map(permission -> permission.getOperation())
				.collect(Collectors.toList());

		List<String> scopes = this.extractScopes(token);

		if (!scopes.contains("ALL")){

			operations = operations.stream().filter(operation ->  scopes.contains(operation.getName())).collect(Collectors.toList());

		}

		return operations;
	}

	private List<String> extractScopes(Jwt token) {
		return (List<String>)token.getClaims().get("scope");
	}

	private Boolean isAuthorized(String endpoint, List<Operation> operations) {
		Boolean isAuthorized = operations.stream().anyMatch(operation -> {
			String basePath = operation.getModule().getBasePath();
			Pattern pattern = Pattern.compile(basePath.concat(operation.getPath()));
			Matcher matcher = pattern.matcher(endpoint);
			return matcher.matches();
		});
		return isAuthorized;
	}

}
