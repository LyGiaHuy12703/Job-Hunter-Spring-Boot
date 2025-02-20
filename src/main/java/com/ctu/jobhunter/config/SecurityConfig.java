package com.ctu.jobhunter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).cors().and().csrf().disable();
        return httpSecurity.build();

        // httpSecurity.cors().and().csrf().disable()
        //     .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        // return httpSecurity.build();
    }
}
