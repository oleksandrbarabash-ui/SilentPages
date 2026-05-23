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
import com.library.dto.LoginRequest;
import com.library.dto.AuthResponse;
import com.library.security.JwtProvider;


/**
 * Сервіс для обробки логіки реєстрації та автентифікації користувачів.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtProvider jwtProvider, JwtProvider jwtProvider1) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
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

    /**
     * Перевіряє наявність користувача, правильність пароля через BCrypt та генерує JWT-токен.
     * Реалізує безпечну логіку авторизації (входу) користувача.
     */
    public AuthResponse login(LoginRequest request) {
        // 1. Шукаємо користувача за email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Невірний email або пароль"));

        // 2. Порівнюємо сирий пароль з хешованим із бази даних через BCrypt
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Невірний email або пароль");
        }

        // 3. Якщо все супер — генеруємо токен
        String token = jwtProvider.generateToken(user);

        // 4. Збираємо базову інфо
        UserDto userDto = new UserDto(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPhone(),
                user.getRole().getName()
        );

        return new AuthResponse(token, userDto);
    }
}