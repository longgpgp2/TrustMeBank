package com.trustme.model;

import java.time.LocalDateTime;

import com.trustme.enums.TransferStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name = "transfers")
@NoArgsConstructor
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private User receiver;
    //need @GreaterThan annotation
    @Column(nullable = false,  columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double amount = 0.0;

    @CreatedDate
    private LocalDateTime timestamp;

    private String description;

    /**
     * Transfer status is enum: Pending (default), Completed and Failed
     */
    private TransferStatus status = TransferStatus.PENDING;
}
