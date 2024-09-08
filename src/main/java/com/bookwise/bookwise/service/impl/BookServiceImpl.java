package com.bookwise.bookwise.service.impl;

import com.bookwise.bookwise.dto.book.BookInDTO;
import com.bookwise.bookwise.dto.book.BookOutDTO;
import com.bookwise.bookwise.dto.category.CategoryDTO;
import com.bookwise.bookwise.entity.Book;
import com.bookwise.bookwise.entity.Category;
import com.bookwise.bookwise.exception.ResourceAlreadyExistsException;
import com.bookwise.bookwise.exception.ResourceNotFoundException;
import com.bookwise.bookwise.mapper.BookMapper;
import com.bookwise.bookwise.mapper.CategoryMapper;
import com.bookwise.bookwise.repository.BookRepository;
import com.bookwise.bookwise.repository.CategoryRepository;
import com.bookwise.bookwise.repository.IssuanceRepository;
import com.bookwise.bookwise.service.IBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements IBookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final IssuanceRepository issuanceRepository;

    @Override
    public List<BookOutDTO> getAllBooks(Sort sort) {
        return bookRepository.findAll(sort).stream()
                .map(book -> BookMapper.mapToBookOutDTO(book, new BookOutDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<BookOutDTO> getBooks(Pageable pageable, String search) {
        Page<Book> bookPage;
        if (search != null && !search.isEmpty()) {
            bookPage = bookRepository.findByTitleContainingIgnoreCase(search, pageable);
        } else {
            bookPage = bookRepository.findAll(pageable);
        }

        return  bookPage.map(book -> BookMapper.mapToBookOutDTO(book, new BookOutDTO()));
    }

    @Override
    public BookOutDTO getBookByTitle(String title) {

        Book book = bookRepository.findByTitle(title).orElseThrow(
                () -> new ResourceNotFoundException("Book", "title", title)
        );

        BookOutDTO bookOutDTO = BookMapper.mapToBookOutDTO(book, new BookOutDTO());
        return bookOutDTO;

    }

    @Override
    public BookOutDTO getBookById(Long id) {

        Book book = bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Book", "id", id.toString())
        );

        BookOutDTO bookOutDTO = BookMapper.mapToBookOutDTO(book, new BookOutDTO());
        return bookOutDTO;

    }

    @Override
    public BookOutDTO deleteBookById(Long id) {

        Book book = bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Book", "id", id.toString())
        );

        boolean isBookIssued = issuanceRepository.existsByBookIdAndStatus(book.getId(), "Issued");

        if (isBookIssued) {
            throw new IllegalStateException("The book is currently issued and cannot be deleted.");
        }

        issuanceRepository.deleteAllByBookIn(Collections.singletonList(book));
        bookRepository.deleteById(book.getId());

        BookOutDTO bookOutDTO = BookMapper.mapToBookOutDTO(book, new BookOutDTO());
        return bookOutDTO;

    }

    @Override
    public BookOutDTO createBook(BookInDTO bookInDTO) {

        Optional<Book> optionalBook = bookRepository.findByTitle(bookInDTO.getTitle());

        if (optionalBook.isPresent()) {
            throw new ResourceAlreadyExistsException("Book already exists with the same title.");
        }

        Book book = BookMapper.mapToBook(bookInDTO, new Book(), categoryRepository);
        Book savedBook = bookRepository.save(book);

        BookOutDTO bookOutDTO = BookMapper.mapToBookOutDTO(savedBook, new BookOutDTO());

        return bookOutDTO;

    }

    @Override
    public BookOutDTO updateBook(Long id, BookInDTO bookInDTO) {

        Book book = bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Book", "id", id.toString())
        );

        Long prevTotalQty = book.getTotalQty();
        Long prevAvlQty = book.getAvlQty();

        book = BookMapper.mapToBook(bookInDTO, book, categoryRepository);

        Long newTotalQty = bookInDTO.getTotalQty();
        Long newAvlQty = prevAvlQty + (newTotalQty-prevTotalQty);
        if (newAvlQty < 0) {
            newAvlQty = 0L;
        }
        book.setAvlQty(newAvlQty);

        Book savedBook = bookRepository.save(book);

        BookOutDTO bookOutDTO = BookMapper.mapToBookOutDTO(savedBook, new BookOutDTO());

        return bookOutDTO;

    }
}
