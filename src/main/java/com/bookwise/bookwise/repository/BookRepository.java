package com.bookwise.bookwise.repository;

import com.bookwise.bookwise.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

//    List<Book> findAllByTitle(String title);
    List<Book> findAllByAuthor(String author);

    @Query("SELECT b FROM Book b WHERE b.category.id = :id")
    List<Book> findAllByCategory(Long id);

    Optional<Book> findByTitle(String title);


}
