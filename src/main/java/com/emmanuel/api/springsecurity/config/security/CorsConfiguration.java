package com.emmanuel.api.springsecurity.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfiguration {

    @Bean
    CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();

        config.setAllowedOrigins(List.of("http://127.0.0.1:5500")); // I'll allow only this domain.
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // I allow only the previous http verbs.
        config.setAllowedHeaders(List.of("*")); // I am allowing all the headers.
        config.setAllowCredentials(true); // I'm going to allow credentials if the client sends them.

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
