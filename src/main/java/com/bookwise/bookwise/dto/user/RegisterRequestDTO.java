package com.bookwise.bookwise.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class RegisterRequestDTO {

    @NotEmpty(message = "Name can not be a null or empty")
    private String name;

    @NotEmpty(message = "Email can not be a null or empty")
    private String email;

    @NotEmpty(message = "Mobile number can not be a null or empty")
    private String mobileNumber;

    private String password;

    private String role;

}
