package com.bookwise.bookwise.repository;

import com.bookwise.bookwise.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

//    List<Book> findAllByTitle(String title);
//    List<Book> findAllByAuthor(String author);
//
    @Query("SELECT b FROM Book b WHERE b.category.id = :id")
    List<Book> findAllByCategory(Long id);

    Optional<Book> findByTitle(String title);

    @Query("SELECT SUM(b.totalQty) FROM Book b")
    Long getTotalBooksCount();

    @Query("SELECT SUM(b.avlQty) FROM Book b")
    Long getTotalAvailableBooks();

    Long countByAvlQtyGreaterThan(int qty);

    @Modifying
    @Query("DELETE FROM Book b WHERE b.category.id = :categoryId")
    void deleteAllByCategory(@Param("categoryId") Long categoryId);
}
