package com.trustme.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="transfers")
public class Transfer {
    @Id
    @GeneratedValue
    Long id;
    Long sentBy;
    Long receivedBy;
    @CreatedDate
    Date createdAt;
    Date updatedAt;
}
