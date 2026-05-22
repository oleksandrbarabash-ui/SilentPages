package com.library.repository.specification;

import com.library.model.Book;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.JoinType;

/**
 * Компонент побудови динамічних критеріїв пошуку (Spring Data JPA Specifications).
 * Набір правил для генерації SQL-умов "WHERE" безпосередньо під час виконання програми.
 * Дозволяє комбінувати будь-яку кількість фільтрів (пошук, жанр, статус)
 * в один SQL-запит, уникнувши написання десятків окремих методів у репозиторії.
 */
public class BookSpecifications {

    /**
     * Формує умову для текстового пошуку за назвою АБО автором книги.
     * Реалізує логіку "LOWER(field) LIKE %text%", що робить пошук регістронезалежним.
     */
    public static Specification<Book> hasSearchText(String text) {
        return (root, query, criteriaBuilder) -> {
            if (text == null || text.trim().isEmpty()) return null; // Якщо пошук порожній, ігноруємо умову
            String pattern = "%" + text.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), pattern)
            );
        };
    }

    /**
     * Формує SQL-умову порівняння за ID жанру.
     * Додає до запиту обмеження "WHERE genre_id = ?", якщо користувач обрав конкретний жанр.
     */
    public static Specification<Book> hasGenreId(Integer genreId) {
        return (root, query, criteriaBuilder) -> {
            if (genreId == null) return null;
            return criteriaBuilder.equal(root.get("genre").get("id"), genreId);
        };
    }

    /**
     * Формує SQL-умову порівняння за ID статусу книги.
     * Додає до запиту обмеження "WHERE book_status_id = ?", якщо увімкнено фільтр за доступністю.
     */
    public static Specification<Book> hasStatusId(Integer statusId) {
        return (root, query, criteriaBuilder) -> {
            if (statusId == null) return null;
            return criteriaBuilder.equal(root.get("bookStatus").get("id"), statusId);
        };
    }

    /**
     * Оптимізує завантаження зв'язаних таблиць через LEFT JOIN FETCH.
     * Вирішує критичну проблему продуктивності ORM (проблему N+1).
     * Змушує Hibernate витягувати книгу, її жанр та статус за ОДИН SQL-запит до бази даних.
     * Перевірка "getResultType()" потрібна, щоб FETCH не ламав системні COUNT-запити пагінації.
     */
    public static Specification<Book> fetchGenreAndStatus() {
        return (root, query, criteriaBuilder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch("genre", JoinType.LEFT);
                root.fetch("bookStatus", JoinType.LEFT);
            }
            return null;
        };
    }
}