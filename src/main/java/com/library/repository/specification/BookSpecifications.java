package com.library.repository.specification;

import com.library.model.Book;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.JoinType;

public class BookSpecifications {

    // 1. Фильтр по поисковому запросу (название или автор)
    public static Specification<Book> hasSearchText(String text) {
        return (root, query, criteriaBuilder) -> {
            if (text == null || text.trim().isEmpty()) return null;
            String pattern = "%" + text.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), pattern)
            );
        };
    }

    // 2. Фильтр по жанру
    public static Specification<Book> hasGenreId(Integer genreId) {
        return (root, query, criteriaBuilder) -> {
            if (genreId == null) return null;
            return criteriaBuilder.equal(root.get("genre").get("id"), genreId);
        };
    }

    // 3. Фильтр по статусу
    public static Specification<Book> hasStatusId(Integer statusId) {
        return (root, query, criteriaBuilder) -> {
            if (statusId == null) return null;
            return criteriaBuilder.equal(root.get("bookStatus").get("id"), statusId);
        };
    }

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
