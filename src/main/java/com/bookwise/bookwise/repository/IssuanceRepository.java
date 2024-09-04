package com.bookwise.bookwise.repository;

import com.bookwise.bookwise.entity.Book;
import com.bookwise.bookwise.entity.Issuance;
import com.bookwise.bookwise.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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


    @Query("SELECT COUNT(DISTINCT i.user.id) FROM Issuance i WHERE i.status = :status")
    Long countDistinctUsersByStatus(@Param("status") String status);

    @Query("SELECT COUNT(DISTINCT i.user.id) FROM Issuance i WHERE i.status = :status AND i.issuanceType = :issuanceType")
    Long countDistinctUsersByStatusAndIssuanceType(@Param("status") String status, @Param("issuanceType") String issuanceType);

    @Query("SELECT COUNT(DISTINCT i.user.id) FROM Issuance i WHERE i.issuanceType = 'In house' AND DATE(i.issueTime) = CURRENT_DATE")
    Long countDistinctUsersInLibraryToday();


    @Query("SELECT i FROM Issuance i WHERE i.user.id = :id")
    List<Issuance> findAllByUserId(Long id);

    @Query("SELECT i FROM Issuance i WHERE i.user.mobileNumber = :mobileNumber")
    List<Issuance> findAllByUserMobile(String mobileNumber);

    @Query("SELECT i FROM Issuance i WHERE i.book.id = :id")
    List<Issuance> findAllByBookId(Long id);

    boolean existsByBookCategoryIdAndStatus(Long categoryId, String status);

    @Query("SELECT COUNT(i) FROM Issuance i WHERE i.book.category.id = :categoryId AND i.status = 'Issued'")
    Long countIssuedBooksInCategory(@Param("categoryId") Long categoryId);

    @Modifying
    @Transactional
    void deleteAllByBookIn(List<Book> books);

    @Modifying
    @Transactional
    void deleteAllByUserIn(List<User> users);

    boolean existsByBookIdAndStatus(Long bookId, String status);
    boolean existsByUserIdAndStatus(Long userId, String status);

}
