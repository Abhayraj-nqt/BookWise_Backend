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

//        http.requiresChannel(rcc -> rcc.anyRequest().requiresInsecure());  // Only HTTP

        http
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrfConfig -> csrfConfig.disable())
                .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:5173/"));
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
                .requestMatchers("/api/login", "/api/error").permitAll()
                .requestMatchers("/api/register").hasRole("ADMIN")
                .requestMatchers("/api/current-user").authenticated()

//                Category routes
                .requestMatchers("/api/categories", "/api/category-count").permitAll()
                .requestMatchers("/api/category", "/api/category/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/category/**").permitAll()

//                Book routes
                .requestMatchers( "/api/books/**").hasRole("ADMIN")
                .requestMatchers( "/api/book/**").hasRole("ADMIN")
                .requestMatchers( "/api/book").hasRole("ADMIN")
                .requestMatchers("/api/books", "/api/books/title-count","/api/books/total-count").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/book/**").permitAll()
                .requestMatchers("/api/book/history/**").hasRole("ADMIN")


//                User routes
                .requestMatchers("/api/user/history/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers( "/api/user/**").hasRole("ADMIN")
                .requestMatchers( "/api/users").hasRole("ADMIN")
                .requestMatchers( "/api/user-count").hasRole("ADMIN")
                .requestMatchers( HttpMethod.GET, "/api/user/**").hasAnyRole("ADMIN", "USER")


//                Issuance routes
                .requestMatchers("/api/issuance/**").hasRole("ADMIN")
                .requestMatchers("/api/issuances").authenticated()
                .requestMatchers("/api/users/active-count").hasRole("ADMIN")
                .requestMatchers( HttpMethod.GET, "/api/issuance/**").hasAnyRole("ADMIN", "USER")


//                        .requestMatchers("/**").permitAll()

        );

        http
//                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
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

//    @Bean
//    CompromisedPasswordChecker compromisedPasswordChecker() {
//        return new HaveIBeenPwnedRestApiPasswordChecker();
//    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        CustomUsernamePwdAuthenticationProvider authenticationProvider = new CustomUsernamePwdAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

}
