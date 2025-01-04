package com.trustme.dto.request;

import com.trustme.dto.LoanDto;
import com.trustme.enums.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequest{
    private Double amount;

    private Double interestRate;

    private Long duration;
}
