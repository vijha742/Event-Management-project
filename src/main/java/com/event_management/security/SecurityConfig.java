package com.event_management.security;

import com.event_management.service.SessionValidationService;

import jakarta.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Base64;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired private CustomOAuth2UserService customOAuth2UserService;

        String targetUrl = "http://localhost:5173"; // Your React app URL

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Enable CSRF protection instead of disabling it
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        authorize ->
                                authorize
                                        .requestMatchers(
                                                "/oauth2/**",
                                                "/login",
                                                "/",
                                                "/api/session/validate")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated())
                .oauth2Login(
                        oauth2 ->
                                oauth2.authorizationEndpoint(
                                                endpoint -> endpoint.baseUri("/oauth2/authorize"))
                                        .redirectionEndpoint(
                                                endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                                        .userInfoEndpoint(
                                                endpoint ->
                                                        endpoint.userService(
                                                                customOAuth2UserService))
                                        .successHandler(
                                                (request, response, authentication) -> {
                                                    // Generate session token and add to response
                                                    String sessionToken =
                                                            generateSessionToken(authentication);
                                                    Cookie sessionCookie =
                                                            new Cookie(
                                                                    "SESSION-TOKEN", sessionToken);
                                                    sessionCookie.setHttpOnly(true);
                                                    sessionCookie.setSecure(
                                                            true); // For HTTPS environments
                                                    sessionCookie.setPath("/");
                                                    response.addCookie(sessionCookie);

                                                    if (targetUrl == null) {
                                                        targetUrl = "/"; // Default redirect
                                                    }
                                                    response.sendRedirect(targetUrl);
                                                }))
                // Add session management configuration
                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                        .maximumSessions(1)
                                        .expiredUrl("/login?expired"));

        return http.build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                Arrays.asList("http://localhost:5173")); // Your React app URL
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Helper method to generate a session token
    private String generateSessionToken(Authentication authentication) {
        // In a real implementation, this would be more secure
        // This is a simplified example
        String username = authentication.getName();
        long timestamp = System.currentTimeMillis();
        String rawToken = username + ":" + timestamp;

        // Basic encoding - in production use a more secure method
        return Base64.getEncoder().encodeToString(rawToken.getBytes());
    }

    // For validating session tokens manually
    @Bean
    public SessionValidationService sessionValidationService() {
        return new SessionValidationService();
    }
}
