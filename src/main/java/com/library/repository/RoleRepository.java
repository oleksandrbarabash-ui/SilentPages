package com.library.repository;

import com.library.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Репозиторій для роботи з таблицею ролей (role).
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Пошук ролі за її назвою (наприклад, "client").
     */
    Optional<Role> findByName(String name);
}