package com.bookwise.bookwise.service;

import com.bookwise.bookwise.dto.auth.LoginRequestDTO;
import com.bookwise.bookwise.dto.auth.LoginResponseDTO;
import com.bookwise.bookwise.dto.user.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface IAuthService {

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    LoginResponseDTO getUserByToken(String token);

}
