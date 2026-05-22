package com.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Конфігураційний клас для налаштування засобів безпеки додатку.
 */
@Configuration
public class SecurityConfig {

    /**
     * Створення компонента PasswordEncoder, що використовує сильний алгоритм хешування BCrypt.
     * Для безпечного ширування паролів користувачів перед збереженням у БД.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}