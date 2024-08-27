package com.bookwise.bookwise.controller;

import com.bookwise.bookwise.dto.book.BookInDTO;
import com.bookwise.bookwise.dto.book.BookOutDTO;
import com.bookwise.bookwise.dto.category.CategoryDTO;
import com.bookwise.bookwise.service.IBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class BookController {

    private final IBookService iBookService;

    @GetMapping("/books")
    public ResponseEntity<?> getBooks(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {
        if (page == null || size == null) {
            // Fetch all categories without pagination
            List<BookOutDTO> bookOutDTOList = iBookService.getAllBooks(Sort.by(Sort.Direction.fromString(sortDir), sortBy));
            return ResponseEntity.ok(bookOutDTOList);
        } else {
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
            Page<BookOutDTO> bookDTOPage = iBookService.getBooks(pageable, search);
            return ResponseEntity.status(HttpStatus.OK).body(bookDTOPage);
        }
    }

    @GetMapping("/books/title-count")
    public ResponseEntity<Long> getBookTitleCount() {
        Long count = iBookService.getBookTitleCount();
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    @GetMapping("/books/total-count")
    public ResponseEntity<Long> getTotalBooksCount() {
        Long count = iBookService.getTotalBooksCount();
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    @GetMapping("/books/author/{author}")
    public ResponseEntity<List<BookOutDTO>> getBooksByAuthor(@PathVariable String author) {
        List<BookOutDTO> bookOutDTOList = iBookService.getBooksByAuthor(author);
        return ResponseEntity.status(HttpStatus.OK).body(bookOutDTOList);
    }

    @GetMapping("/books/categoryId/{categoryId}")
    public ResponseEntity<List<BookOutDTO>> getBooksByCategoryId(@PathVariable Long categoryId) {
        List<BookOutDTO> bookOutDTOList = iBookService.getBooksByCategoryId(categoryId);
        return ResponseEntity.status(200).body(bookOutDTOList);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookOutDTO> getBookById(@PathVariable Long id) {
        BookOutDTO bookOutDTO = iBookService.getBookById(id);
        return ResponseEntity.status(HttpStatus.OK).body(bookOutDTO);
    }

    @GetMapping("/book/title/{title}")
    public ResponseEntity<BookOutDTO> getBookByTitle(@PathVariable String title) {
        BookOutDTO bookOutDTO = iBookService.getBookByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(bookOutDTO);
    }

    @PostMapping("/book")
    public ResponseEntity<BookOutDTO> createBook(@RequestBody BookInDTO bookInDTO) {
        BookOutDTO bookOutDTO = iBookService.createBook(bookInDTO);
        return ResponseEntity.status(HttpStatus.OK).body(bookOutDTO);
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<BookOutDTO> updateBook(@PathVariable Long id, @RequestBody BookInDTO bookInDTO) {
        BookOutDTO bookOutDTO = iBookService.updateBook(id, bookInDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookOutDTO);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<BookOutDTO> deleteBookById(@PathVariable Long id) {
        BookOutDTO bookOutDTO = iBookService.deleteBookById(id);
        return ResponseEntity.status(HttpStatus.OK).body(bookOutDTO);
    }

}
