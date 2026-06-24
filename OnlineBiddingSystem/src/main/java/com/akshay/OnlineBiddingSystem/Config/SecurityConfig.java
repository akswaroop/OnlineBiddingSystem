package com.akshay.OnlineBiddingSystem.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf-> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/items", "/items/details", "/h2-console/**", "/register", "/login").permitAll()

                        // Explicitly restrict the entire admin namespace to accounts with the 'ADMIN' role
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .requestMatchers("/items/new", "/bids/place").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // <-- Tells Spring Security to use your custom GET /login route!
                        .loginProcessingUrl("/login") // <-- Tells Spring Security to listen for POST submissions here!
                        .defaultSuccessUrl("/items", true)
                        .permitAll()
                )
                .headers(headers -> headers.frameOptions(frame-> frame.disable()));
                return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
