package com.bookwise.bookwise.service.impl;

import com.bookwise.bookwise.dto.book.BookInDTO;
import com.bookwise.bookwise.dto.book.BookOutDTO;
import com.bookwise.bookwise.dto.category.CategoryDTO;
import com.bookwise.bookwise.entity.Book;
import com.bookwise.bookwise.entity.Category;
import com.bookwise.bookwise.exception.ResourceNotFoundException;
import com.bookwise.bookwise.mapper.BookMapper;
import com.bookwise.bookwise.mapper.CategoryMapper;
import com.bookwise.bookwise.repository.BookRepository;
import com.bookwise.bookwise.repository.CategoryRepository;
import com.bookwise.bookwise.service.IBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements IBookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<BookOutDTO> getBooksByAuthor(String author) {
        List<Book> bookList = bookRepository.findAllByAuthor(author);
        List<BookOutDTO> bookOutDTOList = new ArrayList<>();
        bookList.forEach(book -> bookOutDTOList.add(BookMapper.mapToBookOutDTO(book, new BookOutDTO())));

        return bookOutDTOList;
    }

    @Override
    public List<BookOutDTO> getBooksByCategoryId(Long id) {
        List<Book> bookList = bookRepository.findAllByCategory(id);

        List<BookOutDTO> bookOutDTOList = new ArrayList<>();
        bookList.forEach(book -> bookOutDTOList.add(BookMapper.mapToBookOutDTO(book, new BookOutDTO())));

        return bookOutDTOList;
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
    public BookOutDTO deleteBookByTitle(String title) {

        Book book = bookRepository.findByTitle(title).orElseThrow(
                () -> new ResourceNotFoundException("Book", "title", title)
        );

        bookRepository.deleteById(book.getId());

        BookOutDTO bookOutDTO = BookMapper.mapToBookOutDTO(book, new BookOutDTO());
        return bookOutDTO;

    }

    @Override
    public BookOutDTO deleteBookById(Long id) {

        Book book = bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Book", "id", id.toString())
        );

        bookRepository.deleteById(book.getId());

        BookOutDTO bookOutDTO = BookMapper.mapToBookOutDTO(book, new BookOutDTO());
        return bookOutDTO;

    }

    @Override
    public BookOutDTO createBook(BookInDTO bookInDTO) {

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

        book = BookMapper.mapToBook(bookInDTO, book, categoryRepository);

        Book savedBook = bookRepository.save(book);

        BookOutDTO bookOutDTO = BookMapper.mapToBookOutDTO(savedBook, new BookOutDTO());

        return bookOutDTO;

    }
}
