package com.trustme.model;

import com.trustme.enums.TransferStatus;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

public class Saving {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saver_id", referencedColumnName = "id")
    private User saver;

    @Column(nullable = false,  columnDefinition = "DOUBLE DEFAULT 0")
    private Double amount = 0.0;

    @Column(nullable = false,  columnDefinition = "DOUBLE DEFAULT 0")
    private Double interestRate = 0.0;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Common status for transferring, saving, loaning
     */
    @Column(nullable = false, columnDefinition = "DEFAULT CHAR PENDING")
    private TransferStatus status = TransferStatus.PENDING;

    /**
     * Type of saving => should be enum
     */
    @Column(nullable = false)
    private String type;
}
