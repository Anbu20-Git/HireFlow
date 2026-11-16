package com.hireflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .authorizeHttpRequests(auth -> auth

                // Public pages
                .requestMatchers(
                    "/",
                    "/login",
                    "/register",
                    "/css/**",
                    "/js/**",
                    "/images/**"
                ).permitAll()

                // Admin access
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Recruiter access
                .requestMatchers("/recruiter/**").hasRole("RECRUITER")

                // Candidate access
                .requestMatchers("/candidate/**").hasRole("CANDIDATE")

                // Other pages require authentication
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .failureUrl("/login?error")
                .successHandler((request, response, authentication) -> {

                    boolean isAdmin = authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                            authority.getAuthority().equals("ROLE_ADMIN"));

                    boolean isRecruiter = authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                            authority.getAuthority().equals("ROLE_RECRUITER"));

                    boolean isCandidate = authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                            authority.getAuthority().equals("ROLE_CANDIDATE"));

                    if (isAdmin) {
                        response.sendRedirect("/admin/dashboard");

                    } else if (isRecruiter) {
                        response.sendRedirect("/recruiter/dashboard");

                    } else if (isCandidate) {
                        response.sendRedirect("/candidate/dashboard");

                    } else {
                        response.sendError(403, "Access Denied");
                    }
                })
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }
}