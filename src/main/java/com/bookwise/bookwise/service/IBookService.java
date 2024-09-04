package com.bookwise.bookwise.service;

import com.bookwise.bookwise.dto.book.BookInDTO;
import com.bookwise.bookwise.dto.book.BookOutDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IBookService {

    List<BookOutDTO> getAllBooks(Sort sort);
    Page<BookOutDTO> getBooks(Pageable pageable, String search);
    BookOutDTO getBookByTitle(String title);
    BookOutDTO getBookById(Long id);

    BookOutDTO createBook(BookInDTO bookInDTO);
    BookOutDTO updateBook(Long id, BookInDTO bookInDTO);
    BookOutDTO deleteBookById(Long id);

}
