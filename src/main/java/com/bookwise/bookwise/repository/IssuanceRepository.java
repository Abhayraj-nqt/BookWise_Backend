package com.bookwise.bookwise.repository;

import com.bookwise.bookwise.entity.Book;
import com.bookwise.bookwise.entity.Issuance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface IssuanceRepository extends JpaRepository<Issuance, Long> {

    Page<Issuance> findByBookContainingIgnoreCase(String book, Pageable pageable);

    @Query("SELECT i FROM Issuance i " +
            "WHERE (:titles IS NULL OR i.book.title IN :titles) " +
            "AND (:issueTimeFrom IS NULL OR i.issueTime >= :issueTimeFrom) " +
            "AND (:issueTimeTo IS NULL OR i.issueTime <= :issueTimeTo) " +
            "AND (:expectedReturnTimeFrom IS NULL OR i.expectedReturnTime >= :expectedReturnTimeFrom) " +
            "AND (:expectedReturnTimeTo IS NULL OR i.expectedReturnTime <= :expectedReturnTimeTo) " +
            "AND (:status IS NULL OR i.status = :status) " +
            "AND (:type IS NULL OR i.issuanceType = :type)")
    Page<Issuance> filterIssuances(
            @Param("titles") List<String> titles,
            @Param("issueTimeFrom") LocalDateTime issueTimeFrom,
            @Param("issueTimeTo") LocalDateTime issueTimeTo,
            @Param("expectedReturnTimeFrom") LocalDateTime expectedReturnTimeFrom,
            @Param("expectedReturnTimeTo") LocalDateTime expectedReturnTimeTo,
            @Param("status") String status,
            @Param("type") String type,
            Pageable pageable
    );


    @Query("SELECT i FROM Issuance i " +
            "WHERE i.user.id = :userId " +
            "AND (:titles IS NULL OR i.book.title IN :titles) " +
            "AND (:issueTimeFrom IS NULL OR i.issueTime >= :issueTimeFrom) " +
            "AND (:issueTimeTo IS NULL OR i.issueTime <= :issueTimeTo) " +
            "AND (:expectedReturnTimeFrom IS NULL OR i.expectedReturnTime >= :expectedReturnTimeFrom) " +
            "AND (:expectedReturnTimeTo IS NULL OR i.expectedReturnTime <= :expectedReturnTimeTo) " +
            "AND (:status IS NULL OR i.status = :status) " +
            "AND (:type IS NULL OR i.issuanceType = :type)")
    Page<Issuance> filterUserHistory(
            @Param("userId") Long userId,
            @Param("titles") List<String> titles,
            @Param("issueTimeFrom") LocalDateTime issueTimeFrom,
            @Param("issueTimeTo") LocalDateTime issueTimeTo,
            @Param("expectedReturnTimeFrom") LocalDateTime expectedReturnTimeFrom,
            @Param("expectedReturnTimeTo") LocalDateTime expectedReturnTimeTo,
            @Param("status") String status,
            @Param("type") String type,
            Pageable pageable);

    List<Issuance> findAllByExpectedReturnTime(LocalDateTime expectedReturnTime);


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

//    @Query("SELECT i FROM Issuance i WHERE DATE(i.returnTime) = :date")
//    List<Issuance> findAllByReturnDate(LocalDate date);

    // Time-only query
    @Query("SELECT i FROM Issuance i WHERE TIME(i.issueTime) = :time")
    List<Issuance> findAllByIssueTime(LocalTime time);

//    @Query("SELECT i FROM Issuance i WHERE TIME(i.returnTime) = :time")
//    List<Issuance> findAllByReturnTime(LocalTime time);

    // Date range query
    @Query("SELECT i FROM Issuance i WHERE i.issueTime BETWEEN :startDate AND :endDate")
    List<Issuance> findAllByIssueDateRange(LocalDateTime startDate, LocalDateTime endDate);

//    @Query("SELECT i FROM Issuance i WHERE i.returnTime BETWEEN :startDate AND :endDate")
//    List<Issuance> findAllByReturnDateRange(LocalDateTime startDate, LocalDateTime endDate);

    // Specific user and book query
    @Query("SELECT i FROM Issuance i WHERE i.user.id = :userId AND i.book.id = :bookId")
    List<Issuance> findAllByUserIdAndBookId(Long userId, Long bookId);

    // Specific mobile number and book title query
    @Query("SELECT i FROM Issuance i WHERE i.user.mobileNumber = :mobileNumber AND i.book.title = :title")
    List<Issuance> findAllByUserMobileAndBookTitle(String mobileNumber, String title);

}
