package com.bookwise.bookwise.service.impl;

import com.bookwise.bookwise.dto.user.RegisterRequestDTO;
import com.bookwise.bookwise.dto.user.UserDTO;
import com.bookwise.bookwise.entity.User;
import com.bookwise.bookwise.exception.ResourceAlreadyExistsException;
import com.bookwise.bookwise.mapper.UserMapper;
import com.bookwise.bookwise.repository.IssuanceRepository;
import com.bookwise.bookwise.repository.UserRepository;
import com.bookwise.bookwise.service.ISMSService;
import com.bookwise.bookwise.service.IUserService;
import com.bookwise.bookwise.utils.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final ISMSService ismsService;
    private final PasswordEncoder passwordEncoder;
    private final IssuanceRepository issuanceRepository;
    private final Environment env;

    @Override
    public List<UserDTO> getAllUsers(Sort sort) {
        return userRepository.findByRole("ROLE_USER", sort).stream()
                .map(user -> UserMapper.mapToUserDTO(user, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDTO> getUsers(Pageable pageable, String search) {
        Page<User> userPage;
        if (search != null && !search.isEmpty()) {
            userPage = userRepository.findByMobileNumberContainingIgnoreCaseAndRole(search, "ROLE_USER", pageable);
        } else {
            userPage = userRepository.findByRole("ROLE_USER", pageable);
        }

        return userPage.map(user -> UserMapper.mapToUserDTO(user, new UserDTO()));
    }

    @Override
    public UserDTO getUserByMobile(String mobileNumber) {
        User user = userRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new UsernameNotFoundException("User not found for " + mobileNumber)
        );

        UserDTO userDTO = UserMapper.mapToUserDTO(user, new UserDTO());

        return userDTO;

    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found for " + email)
        );

        UserDTO userDTO = UserMapper.mapToUserDTO(user, new UserDTO());

        return userDTO;

    }

    @Override
    public UserDTO deleteUserByMobile(String mobileNumber) {
        User user = userRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new UsernameNotFoundException("User not found for " + mobileNumber)
        );

        boolean isBookIssued = issuanceRepository.existsByUserIdAndStatus(user.getId(), "Issued");

        if (isBookIssued) {
            throw new IllegalStateException("The user has issued some books and cannot be deleted.");
        }

        issuanceRepository.deleteAllByUserIn(Collections.singletonList(user));
        userRepository.deleteById(user.getId());

        UserDTO userDTO = UserMapper.mapToUserDTO(user, new UserDTO());

        return userDTO;
    }

    @Override
    public UserDTO registerUser(RegisterRequestDTO registerRequestDTO) {

        Optional<User> optionalUser = userRepository.findByMobileNumber(registerRequestDTO.getMobileNumber());
        if (optionalUser.isPresent()) {
            throw new ResourceAlreadyExistsException("User already exists for mobile no. " + registerRequestDTO.getMobileNumber());
        }

        optionalUser = userRepository.findByEmail(registerRequestDTO.getEmail());
        if (optionalUser.isPresent()) {
            throw new ResourceAlreadyExistsException("User already exists for email " + registerRequestDTO.getEmail());
        }

        User user = UserMapper.mapToUser(registerRequestDTO, new User());

        String randomPassword = PasswordGenerator.generatePassword(10);
        System.out.println("PASSWORD -> " + randomPassword);

        user.setPassword(passwordEncoder.encode(randomPassword));
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        User savedUser = userRepository.save(user);

        String message = String.format( "\nWelcome %s\n" +
                                        "You have successfully registered to BookWise\n" +
                                        "These are your login credentials\n" +
                                        "Username: %s (OR) %s\n" +
                                        "Password: %s",
                savedUser.getName(),
                savedUser.getMobileNumber(),
                savedUser.getEmail(),
                randomPassword);

        ismsService.sendSms(savedUser.getMobileNumber(), message);

        UserDTO userDTO = UserMapper.mapToUserDTO(savedUser, new UserDTO());
        return  userDTO;
    }

    @Override
    public UserDTO updateUser(String mobileNumber, RegisterRequestDTO registerRequestDTO) {
        User user = userRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new UsernameNotFoundException("User not found for mobile no. " + mobileNumber)
        );

        user = UserMapper.mapToUser(registerRequestDTO, user);

        if (registerRequestDTO.getPassword() != null && registerRequestDTO.getPassword().length() >= 3) {
            String encodedPassword = registerRequestDTO.getPassword();
            byte[] decodedBytes = Base64.getDecoder().decode(encodedPassword);
            String decodedPassword = new String(decodedBytes);
            registerRequestDTO.setPassword(decodedPassword);
            user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        }

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        User updatedUser = userRepository.save(user);

        UserDTO userDTO = UserMapper.mapToUserDTO(updatedUser, new UserDTO());

        return  userDTO;

    }

}
