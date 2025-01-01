package com.trustme.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavingRequest {
    private String name;
    private String type;
    private Double amount;
    private int duration;
}
