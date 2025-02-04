package com.trustme.model;

import com.trustme.enums.TransferStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name="pending_transfers")
@AllArgsConstructor
@NoArgsConstructor
public class PendingTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String description;
    private String senderName;
    private String receiverName;
    private TransferStatus transferStatus = TransferStatus.PENDING;

}
