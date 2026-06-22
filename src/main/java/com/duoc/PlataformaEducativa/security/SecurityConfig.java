package com.duoc.PlataformaEducativa.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/cursos/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/cursos").authenticated()
                .requestMatchers(HttpMethod.PUT, "/cursos/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/cursos/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/inscripciones").authenticated()
                .requestMatchers(HttpMethod.GET, "/inscripciones/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/inscripciones/**").authenticated()
                .requestMatchers("/storage/**").authenticated()
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));

        return http.build();
    }
}