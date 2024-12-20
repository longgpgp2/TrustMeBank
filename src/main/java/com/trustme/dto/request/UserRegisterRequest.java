package com.trustme.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterRequest {
    private String username;
    private String accountName;
    private String password;
    private String pinCode;
}
