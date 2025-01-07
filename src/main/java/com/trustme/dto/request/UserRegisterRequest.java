package com.trustme.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterRequest {
    @NotBlank(message = "Username cannot be empty!")
    private String username;

    @NotBlank(message = "Full Name cannot be empty!")
    private String fullName;

    @NotBlank(message = "Account Name cannot be empty!")
    private String accountName;

    @NotBlank(message = "Email cannot be empty!")
    private String email;

    @NotBlank(message = "Phone Number cannot be empty!")
    private String phone;

    @NotBlank(message = "Password cannot be empty!")
    private String password;

    @NotBlank(message = "Pin Code cannot be empty!")
    private String pinCode;
}
