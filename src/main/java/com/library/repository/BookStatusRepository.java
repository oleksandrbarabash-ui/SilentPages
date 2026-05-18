package com.library.repository;

import io.micrometer.common.lang.NonNull;
import com.library.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookStatusRepository extends JpaRepository<BookStatus, Integer> {
    @NonNull
    List<BookStatus> findAll();
}
