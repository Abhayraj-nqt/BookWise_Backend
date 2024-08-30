package com.bookwise.bookwise.service.impl;

import com.bookwise.bookwise.constants.JWTConstants;
import com.bookwise.bookwise.dto.auth.LoginRequestDTO;
import com.bookwise.bookwise.dto.user.UserDTO;
import com.bookwise.bookwise.service.IAuthService;
import com.bookwise.bookwise.service.IUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final Environment env;
    private final IUserService iUserService;

    @Override
    public UserDTO login(LoginRequestDTO loginRequestDTO) {
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

//        UserDTO customerDTO = iCustomerService.getCustomer(loginRequestDTO.getUsername());
        userDTO.setToken(jwt);
        userDTO.setRole(roles.get(0));

        return userDTO;
    }
}
