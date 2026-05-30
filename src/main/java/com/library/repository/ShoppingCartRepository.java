package com.library.repository;

import com.library.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Репозиторії для роботи з таблицею кошиків у MySQL.
 */
@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {

    /**
     * Пошук кошика за email користувача завдяки зв'язкам Hibernate.
     */
    Optional<ShoppingCart> findByUserEmail(String email);
}