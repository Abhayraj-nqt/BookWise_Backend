package com.bookwise.bookwise.mapper;

import com.bookwise.bookwise.dto.user.RegisterRequestDTO;
import com.bookwise.bookwise.dto.user.UserDTO;
import com.bookwise.bookwise.entity.User;

public final class UserMapper {

    public static UserDTO mapToUserDTO(User user, UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setMobileNumber(user.getMobileNumber());
        userDTO.setRole(user.getRole());

        return userDTO;
    }

    public static User mapToUser(UserDTO userDTO, User user) {
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setMobileNumber(userDTO.getMobileNumber());
        user.setRole(userDTO.getRole());

        return user;
    }

    public static User mapToUser (RegisterRequestDTO registerRequestDTO, User user) {
        user.setName(registerRequestDTO.getName());
        user.setEmail(registerRequestDTO.getEmail());
        user.setMobileNumber(registerRequestDTO.getMobileNumber());
        user.setRole(registerRequestDTO.getRole());

        return user;
    }


}
