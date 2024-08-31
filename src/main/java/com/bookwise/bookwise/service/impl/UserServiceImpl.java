package com.bookwise.bookwise.service.impl;

import com.bookwise.bookwise.constants.JWTConstants;
import com.bookwise.bookwise.dto.user.RegisterRequestDTO;
import com.bookwise.bookwise.dto.user.UserDTO;
import com.bookwise.bookwise.entity.User;
import com.bookwise.bookwise.mapper.UserMapper;
import com.bookwise.bookwise.repository.UserRepository;
import com.bookwise.bookwise.service.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
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
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found for " + email)
        );

        UserDTO userDTO = UserMapper.mapToUserDTO(user, new UserDTO());

        return userDTO;

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
    public List<UserDTO> getAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();

        userList.forEach(user -> userDTOList.add(UserMapper.mapToUserDTO(user, new UserDTO())));
        return userDTOList;
    }

    @Override
    public Long getUserCount() {
        Long userCount = userRepository.count();
        return userCount;
    }

    @Override
    public UserDTO deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found for " + email)
        );

        userRepository.deleteById(user.getId());

        UserDTO userDTO = UserMapper.mapToUserDTO(user, new UserDTO());

        return userDTO;
    }

    @Override
    public UserDTO deleteUserByMobile(String mobileNumber) {
        User user = userRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new UsernameNotFoundException("User not found for " + mobileNumber)
        );

        userRepository.deleteById(user.getId());

        UserDTO userDTO = UserMapper.mapToUserDTO(user, new UserDTO());

        return userDTO;
    }

    @Override
    public UserDTO registerUser(RegisterRequestDTO registerRequestDTO) {
        User user = UserMapper.mapToUser(registerRequestDTO, new User());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }
        User savedUser = userRepository.save(user);
        UserDTO userDTO = UserMapper.mapToUserDTO(savedUser, new UserDTO());
        return  userDTO;
    }

    @Override
    public UserDTO updateUser(String mobileNumber, RegisterRequestDTO registerRequestDTO) {
        User user = userRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new UsernameNotFoundException("User not found for mobile no. " + mobileNumber)
        );

        user = UserMapper.mapToUser(registerRequestDTO, user);
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        User updatedUser = userRepository.save(user);

        UserDTO userDTO = UserMapper.mapToUserDTO(updatedUser, new UserDTO());

        return  userDTO;

    }

    @Override
    public UserDTO getUserByToken(String jwt) {
        String secret = env.getProperty(JWTConstants.JWT_SECRET_KEY, JWTConstants.JWT_SECRET_DEFAULT_VALUE);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parser().verifyWith(secretKey)
                .build().parseSignedClaims(jwt).getPayload();
        String username = String.valueOf(claims.get("username"));

        if (username.contains("@")) {
//                            email
            User user = userRepository.findByEmail(username).orElseThrow(
                    () -> new BadCredentialsException("Invalid Token received!")
            );

            UserDTO userDTO = UserMapper.mapToUserDTO(user, new UserDTO());
            userDTO.setToken(jwt);
            return  userDTO;
        } else {
//                            mobile
            User user = userRepository.findByMobileNumber(username).orElseThrow(
                    () -> new BadCredentialsException("Invalid Token received!")
            );

            UserDTO userDTO = UserMapper.mapToUserDTO(user, new UserDTO());
            userDTO.setToken(jwt);
            return  userDTO;
        }

    }
}
