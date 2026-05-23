package com.library.config;

import com.library.security.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Вмикаємо підтримку Web-безпеки
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Конфігурація ланцюжка фільтрів безпеки (SecurityFilterChain).
     * Визначає рівні доступу до ендпоінтів та підключає наш кастомний JwtFilter.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Вимикаємо CSRF, бо у нас Stateless REST API
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Вимикаємо сесії (куки)
                .authorizeHttpRequests(auth -> auth
                        // Публічні ендпоінти авторизації (вхід та реєстрація доступні всім)
                        .requestMatchers("/api/auth/**").permitAll()
                        // Каталог книг та жанрів можна переглядати без токена
                        .requestMatchers(HttpMethod.GET, "/api/books/**", "/api/genres/**").permitAll()
                        // Наш новий ендпоінт профілю — ТІЛЬКИ для авторизованих користувачів
                        .requestMatchers("/api/users/me").authenticated()
                        // Усі інші запити за замовчуванням теж потребують авторизації
                        .anyRequest().authenticated()
                )
                // Підключаємо наш JWT-фільтр ПЕРЕД стандартним фільтром паролів
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // Налаштування поведінки при спробі неавторизованого доступу (Критерій готовності: 401)
                .exceptionHandling(eh -> eh.authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                    response.setContentType("application/json");
                    response.getWriter().write("{\"status\":401,\"message\":\"Помилка безпеки: Доступ заборонено. Токен відсутній або невалідний.\"}");
                }))
                .build();
    }
}