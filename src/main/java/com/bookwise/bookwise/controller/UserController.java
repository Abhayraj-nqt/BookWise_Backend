package com.bookwise.bookwise.controller;

import com.bookwise.bookwise.constants.UserConstants;
import com.bookwise.bookwise.dto.book.BookOutDTO;
import com.bookwise.bookwise.dto.response.ResponseDTO;
import com.bookwise.bookwise.dto.user.RegisterRequestDTO;
import com.bookwise.bookwise.dto.user.UserDTO;
import com.bookwise.bookwise.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/users")
    public ResponseEntity<?> getAllUser(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {
        if (page == null || size == null) {
            List<UserDTO> userDTOList = iUserService.getAllUsers(Sort.by(sortBy));
            return ResponseEntity.status(HttpStatus.OK).body(userDTOList);
        } else {
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
            Page<UserDTO> userDTOPage = iUserService.getUsers(pageable, search);
            return ResponseEntity.status(HttpStatus.OK).body(userDTOPage);
        }
    }

    @GetMapping("/user/{mobileNumber}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String mobileNumber) {
        UserDTO userDTO = iUserService.getUserByMobile(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        UserDTO savedUser = iUserService.registerUser(registerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(HttpStatus.CREATED.toString(), UserConstants.USER_CREATE_MSG));
    }

    @PutMapping("/user/{mobileNumber}")
    public ResponseEntity<ResponseDTO> updateUser(@PathVariable String mobileNumber, @RequestBody RegisterRequestDTO registerRequestDTO) {
        UserDTO userDTO = iUserService.updateUser(mobileNumber, registerRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK.toString(), UserConstants.USER_UPDATE_MSG));
    }

    @DeleteMapping("/user/{mobileNumber}")
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable String mobileNumber) {
        UserDTO userDTO = iUserService.deleteUserByMobile(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK.toString(), UserConstants.USER_DELETE_MSG));
    }

}
