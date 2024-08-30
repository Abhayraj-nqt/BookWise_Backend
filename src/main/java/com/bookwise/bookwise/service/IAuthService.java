package com.bookwise.bookwise.service;

import com.bookwise.bookwise.dto.auth.LoginRequestDTO;
import com.bookwise.bookwise.dto.user.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface IAuthService {

    UserDTO login(LoginRequestDTO loginRequestDTO);

}
