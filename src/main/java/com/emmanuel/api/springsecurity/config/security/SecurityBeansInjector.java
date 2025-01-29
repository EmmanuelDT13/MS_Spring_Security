package com.emmanuel.api.springsecurity.config.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.emmanuel.api.springsecurity.service.impl.UserDetailsServiceImpl;

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
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		
		System.out.println("Pass1: " + "calve1");
		System.out.println("Pass2: " + "calve2");
		System.out.println("Pass3: " + "calve3");
		
		return encoder;
	}
	
    @Bean
    CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
         
        config.setAllowedOrigins(List.of("http://127.0.0.1:5500")); // I'll allow only this domain.
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // I allow only the previous http verbs.
        config.setAllowedHeaders(List.of("*")); // I am allowing all the headers.
        config.setAllowCredentials(true); // I'm going to allow credentials if the client sends them.

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
	
}
