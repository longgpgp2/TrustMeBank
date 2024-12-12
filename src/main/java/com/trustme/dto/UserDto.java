package com.trustme.dto;

import com.trustme.model.Role;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String id;
    private String username;
    private String accountName;
    private double balance;
    private Role role;
    private Date createdAt;
    private Date updatedAt;
    private boolean isDisabled;
}
