package com.library.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Фільтр безпеки, який виконується один раз на кожен HTTP-запит (OncePerRequestFilter).
 * Перехоплює запити, шукає JWT-токен, валідує його та заповнює SecurityContext.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Отримуємо заголовок Authorization із запиту
        String authHeader = request.getHeader("Authorization");

        // 2. Перевіряємо, чи заголовок містить токен у форматі "Bearer <токен>"
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Вирізаємо сам токен (пропускаємо перші 7 символів "Bearer ")

            // 3. Якщо токен валідний — витягуємо дані
            if (jwtProvider.validateToken(token)) {
                String email = jwtProvider.getEmailFromToken(token);

                // Створюємо об'єкт аутентифікації для Spring Security (пароль нам не потрібен, ставимо null)
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // Тимчасова роль для контексту
                );

                // 4. Зберігаємо користувача в глобальний контекст безпеки Spring Security
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // Передаємо запит далі по ланцюжку іншим фільтрам
        filterChain.doFilter(request, response);
    }
}