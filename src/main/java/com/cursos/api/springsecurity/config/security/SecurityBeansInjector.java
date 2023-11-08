package com.cursos.api.springsecurity.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.cursos.api.springsecurity.service.impl.UserDetailsServiceImpl;

@Configuration
public class SecurityBeansInjector {

	@Autowired
	AuthenticationConfiguration authenticationConfiguration;
	
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Bean
	AuthenticationManager generateAuthenticatioManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	AuthenticationProvider generateAuthenticationProvider() {
		DaoAuthenticationProvider my_daoauthenticationprovider = new DaoAuthenticationProvider();
		my_daoauthenticationprovider.setPasswordEncoder(this.generatePasswordEncoder());
		my_daoauthenticationprovider.setUserDetailsService(userDetailsServiceImpl);
		return my_daoauthenticationprovider;
	}
	
	@Bean
	PasswordEncoder generatePasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
