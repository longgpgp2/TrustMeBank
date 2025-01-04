package com.trustme.dto.request;

import com.trustme.enums.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoanEditRequest {

    private LoanStatus status;

    private Long duration;
}
