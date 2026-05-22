package com.library.repository;

import com.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Репозиторій для роботи з таблицею користувачів (app_user).
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Пошук користувача за email. Використовується для перевірки унікальності при реєстрації.
     */
    Optional<User> findByEmail(String email);

    /**
     * Швидка перевірка, чи існує вже користувач із таким email.
     */
    boolean existsByEmail(String email);
}