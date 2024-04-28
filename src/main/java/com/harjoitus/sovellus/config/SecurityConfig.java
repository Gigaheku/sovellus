package com.harjoitus.sovellus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.harjoitus.sovellus.service.UserDetailsServiceImpl;

@Configuration
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/css/**").permitAll() // Enable CSS when logged out
                .requestMatchers("/h2-console/**").permitAll() // Enable access to H2 Console
                .requestMatchers("/").permitAll()
                .requestMatchers("/login", "/registration", "/register", "/css/styles.css").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/user/**").hasAuthority("ROLE_USER")
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/movies", true)
                .permitAll())
            .logout(logout -> logout
                .permitAll())
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")) // Disable CSRF for H2 Console
            .headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }
}

