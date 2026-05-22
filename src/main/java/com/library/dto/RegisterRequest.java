package com.library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO для отримання вхідних даних при реєстрації нового користувача.
 * Збирає дані з HTTP-запиту та автоматично валідує їх перед обробкою сервісом.
 */
public class RegisterRequest {

    @NotBlank(message = "Ім'я є обов'язковим для заповнення")
    private String firstname;

    @NotBlank(message = "Прізвище є обов'язковим для заповнення")
    private String lastname;

    @NotBlank(message = "Email є обов'язковим для заповнення")
    @Email(message = "Некоректний формат email адреси")
    private String email;

    private String phone;

    @NotBlank(message = "Пароль є обов'язковим для заповнення")
    @Size(min = 6, message = "Пароль має містити щонайменше 6 символів")
    private String password;

    // Геттери та сеттери обов'язкові для Jackson
    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
