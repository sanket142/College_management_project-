package com.college.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Standalone PasswordEncoder config — kept separate from SecurityConfig
 * to break the circular dependency chain:
 *
 *   SecurityConfig → CustomUserDetailsService → UserRepository
 *   DataInitializer → PasswordEncoder
 *
 * Previously both SecurityConfig and DataInitializer shared the same
 * config class, causing Spring to see a cycle. Moving the encoder here
 * makes it a leaf bean with no upstream dependencies.
 */
@Configuration
public class EncoderConfig {

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
