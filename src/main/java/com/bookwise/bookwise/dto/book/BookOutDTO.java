package com.bookwise.bookwise.dto.book;

import com.bookwise.bookwise.dto.category.CategoryDTO;
import lombok.Data;

@Data
public class BookOutDTO {

    private Long id;

    private String title;

    private String author;

    private Long totalQty;

    private Long avlQty;

    private String image;

    private CategoryDTO category;

}
