package com.bookwise.bookwise.dto.user;

import com.bookwise.bookwise.dto.book.BookOutDTO;
import com.bookwise.bookwise.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserHistoryDTO {

    private Long id;

    private BookOutDTO book;

    private LocalDateTime issueTime;

    private LocalDateTime expectedReturnTime;

    private LocalDateTime actualReturnTime;

    private String status;

    private String type;

}
