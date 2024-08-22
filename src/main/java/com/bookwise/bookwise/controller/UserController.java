package com.bookwise.bookwise.controller;

import com.bookwise.bookwise.dto.user.RegisterRequestDTO;
import com.bookwise.bookwise.dto.user.UserDTO;
import com.bookwise.bookwise.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserController {

    private final IUserService iUserService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        UserDTO savedUser = iUserService.registerUser(registerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/user/{mobileNumber}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String mobileNumber) {
        UserDTO userDTO = iUserService.getUserByMobile(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUser() {
        List<UserDTO> userDTOList = iUserService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userDTOList);
    }

    @GetMapping("/user-count")
    public ResponseEntity<Long> getUserCount() {
        Long userCount = iUserService.getUserCount();
        return ResponseEntity.status(200).body(userCount);
    }

    @DeleteMapping("/user/{mobileNumber}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable String mobileNumber) {
        UserDTO userDTO = iUserService.deleteUserByMobile(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PutMapping("/user/{mobileNumber}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String mobileNumber, @RequestBody RegisterRequestDTO registerRequestDTO) {
        UserDTO userDTO = iUserService.updateUser(mobileNumber, registerRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

}
