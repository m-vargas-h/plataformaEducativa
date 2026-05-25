package com.duoc.PlataformaEducativa.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                // Endpoints públicos
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()

                // Cursos
                .requestMatchers(HttpMethod.GET, "/cursos/**").hasAnyRole("ADMIN", "PROFESOR", "ALUMNO")
                .requestMatchers(HttpMethod.POST, "/cursos").hasAnyRole("ADMIN", "PROFESOR")
                .requestMatchers(HttpMethod.PUT, "/cursos/**").hasAnyRole("ADMIN", "PROFESOR")
                .requestMatchers(HttpMethod.DELETE, "/cursos/**").hasAnyRole("ADMIN", "PROFESOR")

                // Inscripciones
                .requestMatchers(HttpMethod.POST, "/inscripciones").hasAnyRole("ADMIN", "ALUMNO")
                .requestMatchers(HttpMethod.GET, "/inscripciones/**").hasAnyRole("ADMIN", "ALUMNO")
                .requestMatchers(HttpMethod.DELETE, "/inscripciones/**").hasAnyRole("ADMIN", "ALUMNO")

                .anyRequest().authenticated()
            )
            // Necesario para que H2 console funcione en iframe
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}