package com.bookwise.bookwise.dto.issuance;

import com.bookwise.bookwise.dto.book.BookOutDTO;
import com.bookwise.bookwise.dto.user.UserDTO;
import com.bookwise.bookwise.entity.Book;
import com.bookwise.bookwise.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IssuanceOutDTO {

    private Long id;

    private UserDTO user;

    private BookOutDTO book;

    private LocalDateTime issueTime;

    private LocalDateTime expectedReturnTime;

    private LocalDateTime actualReturnTime;

    private String status;

    private String issuanceType;

}
