package com.bookwise.bookwise.controller;

import com.bookwise.bookwise.constants.JWTConstants;
import com.bookwise.bookwise.dto.auth.LoginResponseDTO;
import com.bookwise.bookwise.dto.response.ResponseDTO;
import com.bookwise.bookwise.dto.user.UserDTO;
import com.bookwise.bookwise.dto.auth.LoginRequestDTO;
import com.bookwise.bookwise.service.IAuthService;
import com.bookwise.bookwise.service.IUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final Environment env;
    private final IUserService iUserService;
    private final IAuthService iAuthService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {

        LoginResponseDTO loginResponseDTO = iAuthService.login(loginRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).header(JWTConstants.JWT_HEADER,loginResponseDTO.getToken())
                .body(loginResponseDTO);

    }

    @GetMapping("/current-user")
    public ResponseEntity<?> currentUser(@RequestHeader("Authorization") String token) {
        LoginResponseDTO loginResponseDTO = iAuthService.getUserByToken(token);

        if (loginResponseDTO != null) {
            return ResponseEntity.status(HttpStatus.OK).body(loginResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO(HttpStatus.UNAUTHORIZED.toString(), "Invalid token"));
        }
    }

}
