package com.bookwise.bookwise.dto.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class LoginResponseDTO {

    private String name;

    private String mobileNumber;

    private String role;

    private String token;

}
