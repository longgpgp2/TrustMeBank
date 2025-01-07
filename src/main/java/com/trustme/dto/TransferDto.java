package com.trustme.dto;

import com.trustme.enums.TransferStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferDto {
    private Long id;
    private String senderName;
    private String receiverName;
    private double amount;
    private LocalDateTime timeStamp;
    private TransferStatus transferStatus;
    private String description;
}
