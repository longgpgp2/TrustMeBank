package com.trustme.model;

import com.trustme.enums.LoanStatus;
import com.trustme.enums.TransferStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name="loans")
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id", referencedColumnName = "id")
    private User borrower;

    @Column(nullable = false,  columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double amount = 0.0;

    @Column(nullable = false,  columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double interestRate = 0.0;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String description;

    /**
     * Transfer status is enum: Pending (default), Completed and Failed
     */
    private LoanStatus status = LoanStatus.PENDING;
}
