package com.bookwise.bookwise.controller;

import com.bookwise.bookwise.constants.BookConstants;
import com.bookwise.bookwise.dto.book.BookInDTO;
import com.bookwise.bookwise.dto.book.BookOutDTO;
import com.bookwise.bookwise.dto.category.CategoryDTO;
import com.bookwise.bookwise.dto.response.ResponseDTO;
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
    public ResponseEntity<ResponseDTO> createBook(@RequestBody BookInDTO bookInDTO) {
        BookOutDTO bookOutDTO = iBookService.createBook(bookInDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK.toString(), BookConstants.BOOK_CREATE_MSG));
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<ResponseDTO> updateBook(@PathVariable Long id, @RequestBody BookInDTO bookInDTO) {
        BookOutDTO bookOutDTO = iBookService.updateBook(id, bookInDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK.toString(), BookConstants.BOOK_UPDATE_MSG));
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<ResponseDTO> deleteBookById(@PathVariable Long id) {
        BookOutDTO bookOutDTO = iBookService.deleteBookById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK.toString(), BookConstants.BOOK_DELETE_MSG));
    }

}
