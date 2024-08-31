package com.bookwise.bookwise.dto.book;

import com.bookwise.bookwise.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookHistoryDTO {
    private Long id;
    private User user;
    private LocalDateTime issueTime;
    private LocalDateTime expectedReturnTime;
    private LocalDateTime actualReturnTime;
    private String status;
    private String type;

}
