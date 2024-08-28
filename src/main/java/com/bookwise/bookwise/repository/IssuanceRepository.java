package com.bookwise.bookwise.repository;

import com.bookwise.bookwise.entity.Book;
import com.bookwise.bookwise.entity.Issuance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface IssuanceRepository extends JpaRepository<Issuance, Long> {

    Page<Issuance> findByBookContainingIgnoreCase(String book, Pageable pageable);



    @Query("SELECT COUNT(DISTINCT i.user) FROM Issuance i WHERE i.status = 'ISSUED'")
    Long countDistinctUsersByStatus(String status);

    @Query("SELECT i FROM Issuance i WHERE i.user.id = :id")
    List<Issuance> findAllByUserId(Long id);

    @Query("SELECT i FROM Issuance i WHERE i.user.mobileNumber = :mobileNumber")
    List<Issuance> findAllByUserMobile(String mobileNumber);

    @Query("SELECT i FROM Issuance i WHERE i.book.id = :id")
    List<Issuance> findAllByBookId(Long id);

    @Query("SELECT i FROM Issuance i WHERE i.book.title = :title")
    List<Issuance> findAllByBookTitle(String title);

    // Date-only query
    @Query("SELECT i FROM Issuance i WHERE DATE(i.issueTime) = :date")
    List<Issuance> findAllByIssueDate(LocalDate date);

    @Query("SELECT i FROM Issuance i WHERE DATE(i.returnTime) = :date")
    List<Issuance> findAllByReturnDate(LocalDate date);

    // Time-only query
    @Query("SELECT i FROM Issuance i WHERE TIME(i.issueTime) = :time")
    List<Issuance> findAllByIssueTime(LocalTime time);

    @Query("SELECT i FROM Issuance i WHERE TIME(i.returnTime) = :time")
    List<Issuance> findAllByReturnTime(LocalTime time);

    // Date range query
    @Query("SELECT i FROM Issuance i WHERE i.issueTime BETWEEN :startDate AND :endDate")
    List<Issuance> findAllByIssueDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT i FROM Issuance i WHERE i.returnTime BETWEEN :startDate AND :endDate")
    List<Issuance> findAllByReturnDateRange(LocalDateTime startDate, LocalDateTime endDate);

    // Specific user and book query
    @Query("SELECT i FROM Issuance i WHERE i.user.id = :userId AND i.book.id = :bookId")
    List<Issuance> findAllByUserIdAndBookId(Long userId, Long bookId);

    // Specific mobile number and book title query
    @Query("SELECT i FROM Issuance i WHERE i.user.mobileNumber = :mobileNumber AND i.book.title = :title")
    List<Issuance> findAllByUserMobileAndBookTitle(String mobileNumber, String title);

}
