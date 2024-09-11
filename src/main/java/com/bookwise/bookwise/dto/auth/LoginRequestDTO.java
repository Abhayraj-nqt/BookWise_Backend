package com.bookwise.bookwise.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter @Setter @ToString
@RequiredArgsConstructor
public class LoginRequestDTO {

//    It will be either mobileNumber or email
    @NotEmpty(message = "Username can not be a null or empty")
    private String username;

    @NotEmpty(message = "Password can not be a null or empty")
    private String password;

}
