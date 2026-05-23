package com.library.controller;

import com.library.dto.UserDto;
import com.library.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * REST-контролер для роботи з даними користувачів.
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserRestController {

    private final AuthService authService;

    public UserRestController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Ендпоінт: GET /api/users/me
     * Витягує email поточного юзера з контексту безпеки Spring Security та повертає його профіль.
     * Дозволяє фронтенду дізнатися, який користувач зараз увійшов у систему, щоб відобразити його ім'я на екрані.
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUserProfile() {
        // Дістаємо об'єкт аутентифікації з глобального контексту Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Фільтр поклав туди email як головний Principal (ідентифікатор)
        String email = authentication.getName();

        // Запитуємо профіль у сервісу та повертаємо його з кодом 200 OK
        UserDto userProfile = authService.getUserProfile(email);
        return ResponseEntity.ok(userProfile);
    }
}