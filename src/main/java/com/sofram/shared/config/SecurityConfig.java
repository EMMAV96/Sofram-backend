package com.sofram.shared.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofram.auth.infrastructure.security.JwtAuthenticationFilter;
import com.sofram.auth.infrastructure.security.JwtProperties;
import com.sofram.shared.web.security.RestAccessDeniedHandler;
import com.sofram.shared.web.security.RestAuthenticationEntryPoint;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            ObjectMapper objectMapper
    ) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(
                                new RestAuthenticationEntryPoint(objectMapper)
                        )
                        .accessDeniedHandler(
                                new RestAccessDeniedHandler(objectMapper)
                        )
                )

                .authorizeHttpRequests(authorize -> authorize

                        // =========================
                        // ENDPOINTS PÚBLICOS
                        // =========================

                        .requestMatchers(
                                "/health",
                                "/auth/login",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // =========================
                        // USUARIO AUTENTICADO
                        // =========================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/auth/me"
                        ).authenticated()

                        // =========================
                        // RESIDENTES - CONSULTA
                        // =========================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/residentes/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMINISTRATIVO",
                                "MEDICO",
                                "PSICOLOGO",
                                "LIC_ENFERMERIA",
                                "ENFERMERO",
                                "TERAPISTA_OCUPACIONAL"
                        )

                        // RESIDENTES - ADMINISTRACIÓN
                        .requestMatchers(
                                HttpMethod.POST,
                                "/residentes"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMINISTRATIVO"
                        )

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/residentes/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMINISTRATIVO"
                        )

                        // =========================
                        // HABITACIONES
                        // =========================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/habitaciones/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMINISTRATIVO",
                                "MEDICO",
                                "LIC_ENFERMERIA",
                                "ENFERMERO"
                        )

                        .requestMatchers(
                                HttpMethod.POST,
                                "/habitaciones"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMINISTRATIVO"
                        )

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/habitaciones/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMINISTRATIVO"
                        )

                        // =========================
                        // HISTORIA CLÍNICA - CONSULTA
                        // =========================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/historias-clinicas/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "MEDICO",
                                "PSICOLOGO",
                                "LIC_ENFERMERIA",
                                "ENFERMERO"
                        )

                        // Crear historia clínica
                        .requestMatchers(
                                HttpMethod.POST,
                                "/historias-clinicas"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "MEDICO"
                        )

                        // Agregar detalles clínicos
                        .requestMatchers(
                                HttpMethod.POST,
                                "/historias-clinicas/*/detalles"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "MEDICO",
                                "PSICOLOGO",
                                "LIC_ENFERMERIA"
                        )

                        // Antecedentes y alergias
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/historias-clinicas/*/antecedentes-alergias"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "MEDICO",
                                "LIC_ENFERMERIA"
                        )

                        // =========================
                        // ATENCIÓN MÉDICA
                        // =========================

                        .requestMatchers(
                                "/atenciones-medicas/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "MEDICO",
                                "PSICOLOGO",
                                "LIC_ENFERMERIA",
                                "ENFERMERO"
                        )

                        // =========================
                        // EVALUACIONES
                        // =========================

                        .requestMatchers(
                                "/evaluaciones/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "MEDICO",
                                "PSICOLOGO",
                                "LIC_ENFERMERIA",
                                "ENFERMERO"
                        )

                        // =========================
                        // DIAGNÓSTICOS
                        // =========================

                        .requestMatchers(
                                "/diagnosticos/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "MEDICO"
                        )

                        // =========================
                        // TRATAMIENTOS
                        // =========================

                        .requestMatchers(
                                "/tratamientos/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "MEDICO"
                        )

                        // =========================
                        // MEDICACIÓN
                        // =========================

                        .requestMatchers(
                                "/medicaciones/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "MEDICO"
                        )

                        // =========================
                        // CALENDARIOS - CONSULTA
                        // =========================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/calendarios/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMINISTRATIVO",
                                "MEDICO",
                                "PSICOLOGO",
                                "LIC_ENFERMERIA",
                                "ENFERMERO",
                                "TERAPISTA_OCUPACIONAL"
                        )

                        // Calendarios - escritura
                        .requestMatchers(
                                HttpMethod.POST,
                                "/calendarios/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "TERAPISTA_OCUPACIONAL"
                        )

                        // =========================
                        // ACTIVIDADES - CONSULTA
                        // =========================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/actividades/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMINISTRATIVO",
                                "MEDICO",
                                "PSICOLOGO",
                                "LIC_ENFERMERIA",
                                "ENFERMERO",
                                "TERAPISTA_OCUPACIONAL"
                        )

                        // Actividades - creación
                        .requestMatchers(
                                HttpMethod.POST,
                                "/actividades"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "TERAPISTA_OCUPACIONAL"
                        )

                        // =========================
                        // PARTICIPACIÓN / ASISTENCIA
                        // =========================

                        .requestMatchers(
                                "/participaciones-actividades/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "LIC_ENFERMERIA",
                                "ENFERMERO",
                                "TERAPISTA_OCUPACIONAL"
                        )

                        // =========================
                        // PERSONAL - CONSULTA
                        // =========================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/personal/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMINISTRATIVO"
                        )

                        // Personal - creación
                        .requestMatchers(
                                HttpMethod.POST,
                                "/personal/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMINISTRATIVO"
                        )

                        // Personal - actualización
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/personal/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMINISTRATIVO"
                        )

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/personal/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMINISTRATIVO"
                        )

                        // =========================
                        // REPORTES CLÍNICOS
                        // =========================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/reportes/clinico/residentes/*/pdf"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "MEDICO"
                        )

                        // =========================
                        // ADMINISTRADOR
                        // =========================

                        .anyRequest().hasRole("ADMINISTRADOR")
                )

                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }

    @Bean
    AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }
}
