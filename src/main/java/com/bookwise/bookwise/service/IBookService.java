package com.bookwise.bookwise.service;

import com.bookwise.bookwise.dto.book.BookInDTO;
import com.bookwise.bookwise.dto.book.BookOutDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IBookService {

    List<BookOutDTO> getAllBooks(Sort sort);
    Page<BookOutDTO> getBooks(Pageable pageable, String search);
    Long getBookTitleCount();
    Long getTotalBooksCount();

//    List<BookOutDTO> getBooksByTitle(String title);
    List<BookOutDTO> getBooksByAuthor(String author);
    List<BookOutDTO> getBooksByCategoryId(Long id);

    BookOutDTO getBookByTitle(String title);
    BookOutDTO getBookById(Long id);

    BookOutDTO deleteBookByTitle(String title);
    BookOutDTO deleteBookById(Long id);

    BookOutDTO createBook(BookInDTO bookInDTO);

    BookOutDTO updateBook(Long id, BookInDTO bookInDTO);


}
