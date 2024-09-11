package com.bookwise.bookwise.dto.issuance;

import com.bookwise.bookwise.entity.Book;
import com.bookwise.bookwise.entity.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class IssuanceInDTO {

    @NotEmpty(message = "User can not be a null or empty")
    private Long user;

    @NotEmpty(message = "Book can not be a null or empty")
    private Long book;

    @NotEmpty(message = "Return time can not be a null or empty")
    private LocalDateTime returnTime;

    @NotEmpty(message = "Status can not be a null or empty")
    private String status;

    @NotEmpty(message = "Issuance type can not be a null or empty")
    private String issuanceType;

}
