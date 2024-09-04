package com.bookwise.bookwise.dto.book;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter @Setter @ToString
@RequiredArgsConstructor
public class BookInDTO {

    @NotEmpty(message = "Title can not be a null or empty")
    private String title;

    private String author;

    @NotEmpty(message = "Quantity can not be a null or empty")
    private Long totalQty;

    private String image;

    @NotEmpty(message = "Category can not be a null or empty")
    private Long category;

}
