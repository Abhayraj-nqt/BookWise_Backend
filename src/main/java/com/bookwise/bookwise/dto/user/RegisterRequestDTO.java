package com.bookwise.bookwise.dto.user;

import lombok.Data;

@Data
public class RegisterRequestDTO {

    private String name;

    private String email;

    private String mobileNumber;

    private String password;

    private String role = "ROLE_USER";

}
