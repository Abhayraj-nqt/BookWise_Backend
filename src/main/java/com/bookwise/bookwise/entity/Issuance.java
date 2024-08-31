package com.bookwise.bookwise.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Issuance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book")
    private Book book;

    @Column(name = "issue_time")
    private LocalDateTime issueTime;

    @Column(name = "expected_return_time")
    private LocalDateTime expectedReturnTime;

    @Column(name = "actual_return_time")
    private LocalDateTime actualReturnTime;

    private String status;

    @Column(name = "issuance_type")
    private String issuanceType;

    @PrePersist
    protected void onCreate() {
        this.issueTime = LocalDateTime.now();
        this.status = "ISSUED";
    }

}
