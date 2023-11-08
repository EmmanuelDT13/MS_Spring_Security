package com.cursos.api.springsecurity.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import com.cursos.api.springsecurity.config.security.filters.JwtAuthenticationFilter;
import com.cursos.api.springsecurity.config.security.handlers.CustomAccessDeniedHandler;
import com.cursos.api.springsecurity.config.security.handlers.CustomAuthenticationEntryPoint;
import com.cursos.api.springsecurity.persistence.util.ROLE_ENUM;
import com.cursos.api.springsecurity.persistence.util.RolePermission;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class HttpSecurityConfig {

	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	
	@Autowired
	private CustomAccessDeniedHandler customAccessDeniedHandler;
	
	@Autowired
	private AuthorizationManager<RequestAuthorizationContext> my_authorizationManager;
	
	@Bean
	SecurityFilterChain createSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		return 
		httpSecurity
			.csrf(csrfConfig -> csrfConfig.disable())
			.sessionManagement(sessionManagmentConfig -> sessionManagmentConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authenticationProvider(authenticationProvider)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.authorizeHttpRequests(authorizeHttpRequests -> {
				authorizeHttpRequests.anyRequest().access(my_authorizationManager);
			})
			.exceptionHandling( exceptionConfig -> {
				exceptionConfig.authenticationEntryPoint(customAuthenticationEntryPoint);
				exceptionConfig.accessDeniedHandler(customAccessDeniedHandler);
			})
			.build();	
	}
	//Método que realiza la autorización mediante coincidencias http.
	private void createHttpRequests(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizeHttpRequests) {
		
		//Autorización basada en permisos o authorties.
		authorizeHttpRequests.requestMatchers(HttpMethod.GET, "/products").hasAuthority(RolePermission.READ_ALL_PRODUCTS.name());
		authorizeHttpRequests.requestMatchers(HttpMethod.GET, "/products/{productId}").hasAuthority(RolePermission.READ_ONE_CATEGORY.name());
		authorizeHttpRequests.requestMatchers(HttpMethod.POST, "/products").hasAuthority(RolePermission.CREATE_ONE_PRODUCT.name());
		authorizeHttpRequests.requestMatchers(HttpMethod.PUT, "/products/{productId}").hasAuthority(RolePermission.UPDATE_ONE_PRODUCT.name());
		authorizeHttpRequests.requestMatchers(HttpMethod.PUT, "/products/{productId}/disabled").hasAuthority(RolePermission.DISABLE_ONE_PRODUCT.name());
		
		//Autorización basada en roles.
		authorizeHttpRequests.requestMatchers(HttpMethod.GET, "/categories").hasAnyRole(ROLE_ENUM.ACMON.name(), ROLE_ENUM.ASSISTANT_ACMON.name(), ROLE_ENUM.CUSTOMER.name());
		//authorizeHttpRequests.requestMatchers(HttpMethod.GET, "/categories/{categoryId}").hasAnyRole(ROLE.ACMON.name(), ROLE.ASSISTANT_ACMON.name(), ROLE.CUSTOMER.name());
		authorizeHttpRequests.requestMatchers(HttpMethod.POST, "/categories").hasRole(ROLE_ENUM.ACMON.name());
		authorizeHttpRequests.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}").hasAnyRole(ROLE_ENUM.ACMON.name(), ROLE_ENUM.ASSISTANT_ACMON.name());
		authorizeHttpRequests.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}/disabled").hasRole(ROLE_ENUM.ACMON.name());
		
		//Autorización implementando expresiones regulares. Este método es compatible con roles y authorities también.
		//Fíjate cómo en lugar de colocar el id del producto, estamos colocando una expresión regular, la cual establece
		//que recibirá numeros únicamente. Los cuales irán del 0-9 y se podrán repetir indefinidamente.
		authorizeHttpRequests.requestMatchers(RegexRequestMatcher.regexMatcher(HttpMethod.GET, "/categories/[0-9]*")).hasAnyRole(ROLE_ENUM.ACMON.name(), ROLE_ENUM.ASSISTANT_ACMON.name(), ROLE_ENUM.CUSTOMER.name());
		
		//Aquí estamos estableciendo los endpoints que serán públicos para cualquier usuario aunque no esté logeado
		authorizeHttpRequests.requestMatchers(HttpMethod.POST, "/customers/**").permitAll();
		authorizeHttpRequests.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();
		
		//Aquí establecemos que cualquier otra petición no configurada previamente será permitida para todo usuario siempre
		//y cuando esté authenticado o logeado.
		authorizeHttpRequests.anyRequest().authenticated();
		
	}
	
	//Método que realiza la autorización medainte métodos y anotacones.
	private void createHttpRequestsV2(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizeHttpRequests) {
		
		//Aquí estamos estableciendo los endpoints que serán públicos para cualquier usuario aunque no esté logeado
		authorizeHttpRequests.requestMatchers(HttpMethod.POST, "/customers/**").permitAll();
		authorizeHttpRequests.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();
		
		//Aquí establecemos que cualquier otra petición no configurada previamente será permitida para todo usuario siempre
		//y cuando esté authenticado o logeado.
		authorizeHttpRequests.anyRequest().authenticated();
		
	}

	
}
