package com.cursos.api.springsecurity.config.security;

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import com.cursos.api.springsecurity.exception.ObjectNotFoundException;
import com.cursos.api.springsecurity.persistence.entity.User;
import com.cursos.api.springsecurity.persistence.entity.security.Operation;
import com.cursos.api.springsecurity.persistence.entity.security.Role;
import com.cursos.api.springsecurity.persistence.repository.OperationRepository;
import com.cursos.api.springsecurity.persistence.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class MyAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext>{

	@Autowired
	private OperationRepository operationreposiotry;
	
	@Autowired
	private UserRepository UserRepository;
	
	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestContext) {
		
		HttpServletRequest request = requestContext.getRequest();
		String url = this.extractUrl(request);
		String httpMethod = request.getMethod();
		boolean isPublic = this.isPublic(url, httpMethod);
		
		if (isPublic) return new AuthorizationDecision(isPublic);
		
		boolean isGranthed = isGranthed(authentication, request, url, httpMethod, isPublic);
		return new AuthorizationDecision(isGranthed);
	}

	private boolean isGranthed(Supplier<Authentication> authentication, HttpServletRequest request, String url,
			String httpMethod, boolean isPublic) {
		
		if (authentication.get() == null || !(authentication.get() instanceof UsernamePasswordAuthenticationToken)){
			throw new AuthenticationCredentialsNotFoundException("No se encontraron las credenciales de acceso");
		}
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)authentication.get();
		String username = (String) auth.getPrincipal();
		User user = UserRepository.getByUsername(username).orElseThrow(()-> new ObjectNotFoundException("No se pudo"));
		Role my_role = user.getRole();
		List<Operation> operations = my_role.getPermissions().stream().map(permission -> permission.getOperation()).toList();

		boolean isGranthed = operations.stream().anyMatch(operation->{
			String basePath = operation.getModule().getBasePath();
			String methodPath = operation.getPath();
			String httpMethodd = operation.getHttpMethod();
			Pattern pattern = Pattern.compile(basePath.concat(methodPath));
			Matcher matcher = pattern.matcher(url);			
			return matcher.matches() && httpMethodd.equals(httpMethod); 
		});
		return isGranthed;
		
	}
	
	private String extractUrl(HttpServletRequest request) {
		String base_path = request.getContextPath();
		String url = request.getRequestURI();
		url = url.replace(base_path, "");
		return url;
	}
	
	private boolean isPublic(String url, String httpMethod) {
		
		List<Operation> operations = operationreposiotry.findByPublicAccess();
		boolean isPublic = operations.stream().anyMatch(operation -> {
				String basePath = operation.getModule().getBasePath();
				Pattern pattern = Pattern.compile(basePath.concat(operation.getPath()));
				Matcher matcher = pattern.matcher(url);
				return matcher.matches() && httpMethod.equals(operation.getHttpMethod());
			});
		return isPublic;
	}

}
