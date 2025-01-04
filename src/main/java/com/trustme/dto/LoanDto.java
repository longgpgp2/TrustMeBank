package com.trustme.dto;

import com.trustme.enums.LoanStatus;
import com.trustme.enums.SavingStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanDto {
    private Long id;

    private String borrowerAccountName;

    private Double amount;

    private Double interestRate;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    private LoanStatus status;
}
