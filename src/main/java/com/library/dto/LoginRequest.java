package com.library.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO для отримання вхідних даних при спробі аутентифікації (входу).
 */
public class LoginRequest {
    @NotBlank(message = "Email не може бути порожнім")
    private String email;

    @NotBlank(message = "Пароль не може бути порожнім")
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}