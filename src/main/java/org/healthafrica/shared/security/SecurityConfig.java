package org.healthafrica.shared.security;

import lombok.RequiredArgsConstructor;
import org.healthafrica.auth.security.JwtAuthenticationFilter;
import org.healthafrica.shared.tenant.TenantFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security configuration with JWT authentication and role-based access control.
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final PlatformAccessFilter platformAccessFilter;
    private final TenantFilter tenantFilter;

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html")
                        .permitAll()
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        .requestMatchers("/api/platform/**")
                        .hasRole("SUPER_ADMIN")
                        .requestMatchers("/api/users/**")
                        .hasRole("ADMIN")
                        .requestMatchers("/api/analytics/**")
                        .hasAnyRole("ADMIN", "PROGRAM_MANAGER")
                        .requestMatchers("/api/campaigns/**")
                        .hasAnyRole("ADMIN", "PROGRAM_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/audit/**")
                        .hasAnyRole("ADMIN", "PROGRAM_MANAGER")
                        .requestMatchers("/api/integration/dhis2/**")
                        .hasAnyRole("ADMIN", "PROGRAM_MANAGER")
                        .requestMatchers("/api/fhir/**")
                        .hasAnyRole("CLINICIAN", "ADMIN", "PROGRAM_MANAGER")
                        .requestMatchers("/api/openehr/**")
                        .hasAnyRole("CLINICIAN", "ADMIN", "PROGRAM_MANAGER")
                        .requestMatchers("/api/gis/**")
                        .hasAnyRole(
                                "COMMUNITY_HEALTH_WORKER",
                                "CLINICIAN",
                                "PROGRAM_MANAGER",
                                "ADMIN")
                        .requestMatchers("/api/events/**", "/api/sync/**")
                        .hasAnyRole(
                                "COMMUNITY_HEALTH_WORKER",
                                "CLINICIAN",
                                "PROGRAM_MANAGER",
                                "ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(
                        platformAccessFilter,
                        JwtAuthenticationFilter.class)
                .addFilterAfter(
                        tenantFilter,
                        PlatformAccessFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .toList();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(origins);
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
