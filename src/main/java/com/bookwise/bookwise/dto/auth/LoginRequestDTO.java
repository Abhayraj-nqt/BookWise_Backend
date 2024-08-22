package com.bookwise.bookwise.dto.auth;

import lombok.Data;

@Data
public class LoginRequestDTO {

//    It will be either mobileNumber or email
    private String username;

    private String password;

}
