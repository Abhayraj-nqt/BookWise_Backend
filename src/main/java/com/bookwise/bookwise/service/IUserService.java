package com.bookwise.bookwise.service;

import com.bookwise.bookwise.dto.user.RegisterRequestDTO;
import com.bookwise.bookwise.dto.user.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUserService {

    List<UserDTO> getAllUsers(Sort sort);
    Page<UserDTO> getUsers(Pageable pageable, String search);

    UserDTO getUserByMobile(String mobileNumber);
    UserDTO getUserByEmail(String email);
    UserDTO registerUser(RegisterRequestDTO registerRequestDTO);
    UserDTO updateUser(String mobileNumber, RegisterRequestDTO registerRequestDTO);
    UserDTO deleteUserByMobile(String mobileNumber);

}
