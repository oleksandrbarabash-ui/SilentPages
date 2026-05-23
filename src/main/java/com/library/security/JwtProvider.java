package com.library.security;

import com.library.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Компонент для генерації та валідації JSON Web Tokens (JWT).
 * Створює зашифрований рядок (токен), який містить дані користувача
 * та служить його "цифровим паспортом" для фронтенду.
 */
@Component
public class JwtProvider {

    // Секретний ключ (мінімум 32 символи) для підпису токена. У реальних проєктах береться з application.properties
    private final String JWT_SECRET = "super_secret_key_for_library_project_2026_jwt_token";
    // Час життя токена — 24 години (в мілісекундах)
    private final long JWT_EXPIRATION_MS = 86400000;

    /**
     * Що робить: Генерує JWT-токен на основі даних користувача.
     * Запікає всередину токена ID, Email та Роль користувача.
     */
    public String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

        return Jwts.builder()
                .subject(user.getEmail()) // Встановлюємо email як головний ідентифікатор (Subject)
                .claim("id", user.getId()) // Додаємо кастомне поле ID
                .claim("role", user.getRole().getName()) // Додаємо назву ролі
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key) // Підписуємо токен нашим ключем
                .compact();
    }
}