package com.bookwise.bookwise.mapper;

import com.bookwise.bookwise.dto.book.BookInDTO;
import com.bookwise.bookwise.dto.book.BookOutDTO;
import com.bookwise.bookwise.dto.category.CategoryDTO;
import com.bookwise.bookwise.entity.Book;
import com.bookwise.bookwise.entity.Category;
import com.bookwise.bookwise.exception.ResourceNotFoundException;
import com.bookwise.bookwise.repository.CategoryRepository;
import com.bookwise.bookwise.service.ICategoryService;
import lombok.RequiredArgsConstructor;

public final class BookMapper {

    public static BookOutDTO mapToBookOutDTO(Book book, BookOutDTO bookOutDTO) {

        bookOutDTO.setId(book.getId());
        bookOutDTO.setTitle(book.getTitle());
        bookOutDTO.setAuthor(book.getAuthor());
        bookOutDTO.setTotalQty(book.getTotalQty());
        bookOutDTO.setAvlQty(book.getAvlQty());
        bookOutDTO.setImage(book.getImage());
        bookOutDTO.setCategory(CategoryMapper.mapToCategoryDTO(book.getCategory(), new CategoryDTO()));

        return bookOutDTO;
    }

    public static Book mapToBook(BookInDTO bookInDTO, Book book, CategoryRepository categoryRepository) {
        Category category = categoryRepository.findById(bookInDTO.getCategory()).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", bookInDTO.getCategory().toString())
        );
        book.setTitle(bookInDTO.getTitle());
        book.setAuthor(bookInDTO.getAuthor());
//        if (book.getAvlQty() == null || book.getTotalQty() == null) {
//            book.setAvlQty(bookInDTO.getTotalQty());
//        } else {
//            Long newQty = book.getAvlQty() + bookInDTO.getTotalQty() - book.getAvlQty();
//        }
        book.setAvlQty(bookInDTO.getTotalQty());
        book.setTotalQty(bookInDTO.getTotalQty());
        book.setImage(bookInDTO.getImage());
        book.setCategory(category);

        return book;
    }

}
