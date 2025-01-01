package com.trustme.dto;

import com.trustme.enums.SavingStatus;
import com.trustme.enums.TransferStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavingDto {
    private String saverAccountName;

    private Double amount;

    private Double interestRate;

    private String name;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    private SavingStatus status;

    private String type;


}
