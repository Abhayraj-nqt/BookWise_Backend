package com.bookwise.bookwise.service;

import com.bookwise.bookwise.dto.user.RegisterRequestDTO;
import com.bookwise.bookwise.dto.user.UserDTO;

import java.util.List;

public interface IUserService {

    UserDTO getUserByEmail(String email);
    UserDTO getUserByMobile(String mobileNumber);
    List<UserDTO> getAllUsers();
    Long getUserCount();

    UserDTO deleteUserByEmail(String email);
    UserDTO deleteUserByMobile(String mobileNumber);

    UserDTO registerUser(RegisterRequestDTO registerRequestDTO);

    UserDTO updateUser(String mobileNumber, RegisterRequestDTO registerRequestDTO);

    UserDTO getUserByToken(String token);

}
