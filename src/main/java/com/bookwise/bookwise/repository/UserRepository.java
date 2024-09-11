package com.bookwise.bookwise.repository;

import com.bookwise.bookwise.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findByMobileNumberContainingIgnoreCaseAndRole(String mobile, String role, Pageable pageable);
    Page<User> findByRole(String role, Pageable pageable);
    List<User> findByRole(String role, Sort sort);
    List<User> findByRole(String role);

    Optional<User> findByEmail(String email);

    Optional<User> findByMobileNumber(String mobileNumber);

}
