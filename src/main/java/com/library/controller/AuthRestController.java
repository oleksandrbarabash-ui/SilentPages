package com.library.controller;

import com.library.dto.RegisterRequest;
import com.library.dto.UserDto;
import com.library.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST-контролер для операцій автентифікації та реєстрації.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthRestController {

    private final AuthService authService;

    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/register
     * Приймає JSON з даними реєстрації, валідує їх та викликає сервіс створення акаунту.
     * Надає публічний інтерфейс для реєстрації нових клієнтів бібліотеки.
     * @param request Валідований об'єкт запиту реєстрації (перевіряється аннотацією @Valid).
     * @return Об'єкт UserDto зі статусом 201 Created.
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody RegisterRequest request) {
        UserDto createdUser = authService.register(request);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}