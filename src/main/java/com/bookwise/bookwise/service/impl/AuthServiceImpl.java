package com.bookwise.bookwise.service.impl;

import com.bookwise.bookwise.constants.JWTConstants;
import com.bookwise.bookwise.dto.auth.LoginRequestDTO;
import com.bookwise.bookwise.dto.auth.LoginResponseDTO;
import com.bookwise.bookwise.dto.user.UserDTO;
import com.bookwise.bookwise.dto.user.UserHistoryDTO;
import com.bookwise.bookwise.entity.User;
import com.bookwise.bookwise.mapper.UserMapper;
import com.bookwise.bookwise.repository.UserRepository;
import com.bookwise.bookwise.service.IAuthService;
import com.bookwise.bookwise.service.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final Environment env;
    private final IUserService iUserService;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        String encodedPassword = loginRequestDTO.getPassword();
        byte[] decodedBytes = Base64.getDecoder().decode(encodedPassword);
        String decodedPassword = new String(decodedBytes);
        loginRequestDTO.setPassword(decodedPassword);

        String jwt = "";
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequestDTO.getUsername(),
                loginRequestDTO.getPassword());

        Authentication authenticationResponse = authenticationManager.authenticate(authentication);

        if(null != authenticationResponse && authenticationResponse.isAuthenticated()) {
            if (null != env) {
                String secret = env.getProperty(JWTConstants.JWT_SECRET_KEY,
                        JWTConstants.JWT_SECRET_DEFAULT_VALUE);
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                jwt = Jwts.builder().issuer("BookWise").subject("JWT Token")
                        .claim("username", authenticationResponse.getName())
                        .claim("authorities", authenticationResponse.getAuthorities().stream().map(
                                GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                        .issuedAt(new java.util.Date())
                        .expiration(new java.util.Date((new java.util.Date()).getTime() + 30000000))
                        .signWith(secretKey).compact();
            }
        }

        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) authenticationResponse.getAuthorities();
        List<String> roles = new ArrayList<>();
        grantedAuthorities.forEach(grantedAuthority -> roles.add(grantedAuthority.toString()));

        String username = loginRequestDTO.getUsername();

        UserDTO userDTO = new UserDTO();

        if (username.contains("@")) {
//            email
            userDTO = iUserService.getUserByEmail(username);
        } else {
//            mobileNumber
            userDTO = iUserService.getUserByMobile(username);
        }

        userDTO.setRole(roles.get(0));

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        loginResponseDTO.setName(userDTO.getName());
        loginResponseDTO.setMobileNumber(userDTO.getMobileNumber());
        loginResponseDTO.setRole(userDTO.getRole());
        loginResponseDTO.setToken(jwt);

        return loginResponseDTO;
    }

    @Override
    public LoginResponseDTO getUserByToken(String jwt) {
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
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

            loginResponseDTO.setName(userDTO.getName());
            loginResponseDTO.setMobileNumber(userDTO.getMobileNumber());
            loginResponseDTO.setRole(userDTO.getRole());
            loginResponseDTO.setToken(jwt);

            return  loginResponseDTO;
        } else {
//                            mobile
            User user = userRepository.findByMobileNumber(username).orElseThrow(
                    () -> new BadCredentialsException("Invalid Token received!")
            );

            UserDTO userDTO = UserMapper.mapToUserDTO(user, new UserDTO());
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

            loginResponseDTO.setName(userDTO.getName());
            loginResponseDTO.setMobileNumber(userDTO.getMobileNumber());
            loginResponseDTO.setRole(userDTO.getRole());
            loginResponseDTO.setToken(jwt);

            return  loginResponseDTO;
        }
    }
}
