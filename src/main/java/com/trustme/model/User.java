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
@Table(name="users")
public class User {
    @Id
    @GeneratedValue
    Long id;
    String username;
    String accountName;
    String pinCode;
    Double balance;
    String password;
    @ManyToOne
    Role role;
    @CreatedDate
    Date createdAt;
    Date updatedAt;
    boolean isDisabled;
    public boolean validateAccountBalance(Double amount){
        return balance>=amount;
    }
}
