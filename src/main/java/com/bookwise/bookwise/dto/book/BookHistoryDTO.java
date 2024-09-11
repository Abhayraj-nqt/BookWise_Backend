package com.bookwise.bookwise.dto.book;

import com.bookwise.bookwise.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@RequiredArgsConstructor
public class BookHistoryDTO {

    private Long id;

    private User user;

    private LocalDateTime issueTime;

    private LocalDateTime expectedReturnTime;

    private LocalDateTime actualReturnTime;

    private String status;

    private String type;

}
