package com.library.service;

import com.library.dto.RegisterRequest;
import com.library.dto.UserDto;
import com.library.model.Role;
import com.library.model.User;
import com.library.repository.RoleRepository;
import com.library.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервіс для обробки логіки реєстрації та автентифікації користувачів.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Реєструє нового користувача в системі із перевіркою унікальності email,
     * хешуванням пароля та призначенням базової ролі "client".
     * Реалізує бізнес-правила реєстрації та захищає систему від дублікатів акаунтів.
     * @param request Об'єкт із вхідними даними реєстрації.
     * @return Створений профіль у форматі UserDto.
     */
    @Transactional
    public UserDto register(RegisterRequest request) {
        // 1. Перевірка на унікальність email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Користувач з таким email вже зареєстрований");
        }

        // 2. Пошук ролі "client" у базі даних
        Role clientRole = roleRepository.findByName("client")
                .orElseThrow(() -> new RuntimeException("Роль 'client' не знайдено в системі. Заповніть довідник ролей."));

        // 3. Хешування пароля через BCrypt
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // 4. Створення та збереження об'єкта користувача
        User user = new User(
                request.getFirstname(),
                request.getLastname(),
                request.getEmail(),
                request.getPhone(),
                hashedPassword,
                clientRole
        );

        User savedUser = userRepository.save(user);

        // 5. Конвертація у безпечний UserDto
        return new UserDto(
                savedUser.getId(),
                savedUser.getFirstname(),
                savedUser.getLastname(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getRole().getName()
        );
    }
}