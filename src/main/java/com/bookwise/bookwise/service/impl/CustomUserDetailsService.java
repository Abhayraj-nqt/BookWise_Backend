package com.bookwise.bookwise.service.impl;

import com.bookwise.bookwise.entity.User;
import com.bookwise.bookwise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username.contains("@")) {
//            email
            User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User details not found for " + username));
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));

            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);

        } else {
//            mobileNumber
            User user = userRepository.findByMobileNumber(username).orElseThrow(() -> new UsernameNotFoundException("User details not found for " + username));
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));

            return new org.springframework.security.core.userdetails.User(user.getMobileNumber(), user.getPassword(), authorities);
        }


    }
}
