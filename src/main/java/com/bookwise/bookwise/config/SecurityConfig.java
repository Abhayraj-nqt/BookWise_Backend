package com.bookwise.bookwise.config;

import com.bookwise.bookwise.exception.CustomAccessDeniedHandler;
import com.bookwise.bookwise.exception.CustomBasicAuthenticationEntryPoint;
import com.bookwise.bookwise.filter.JWTTokenValidatorFilter;
import com.bookwise.bookwise.provider.CustomUsernamePwdAuthenticationProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrfConfig -> csrfConfig.disable())
                .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Arrays.asList("http://localhost:5173/", "http://localhost:3000/"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setMaxAge(3600L);

                        return config;
                    }
                }));

        http.authorizeHttpRequests((requests) -> requests

//                Other routes
                .requestMatchers("/api/login", "/api/error", "/error").permitAll()
                .requestMatchers("/api/current-user").authenticated()
                .requestMatchers("/api/register", "/api/admin", "/api/admin/statistics").hasRole("ADMIN")

//                Category routes
                .requestMatchers("/api/categories", "/api/category", "/api/category/**").hasRole("ADMIN")

//                Book routes
                .requestMatchers( "/api/books", "/api/book", "/api/books/**", "/api/book/**", "/api/book/history/**").hasRole("ADMIN")

//                User routes
                .requestMatchers("/api/user/history/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers( "/api/user/**", "/api/users").hasRole("ADMIN")
                .requestMatchers( HttpMethod.GET, "/api/user/**").hasAnyRole("ADMIN", "USER")

//                Issuance routes
                .requestMatchers("/api/issuance/**", "/api/issuances").hasRole("ADMIN")
                .requestMatchers("/api/issuances").authenticated()
//                .requestMatchers( HttpMethod.GET, "/api/issuance/**").hasAnyRole("ADMIN", "USER")

        );

        http
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class);

        http.formLogin(formLoginConfig -> formLoginConfig.disable());

        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // by default it is using bcrypt password encoder
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        CustomUsernamePwdAuthenticationProvider authenticationProvider = new CustomUsernamePwdAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

}
