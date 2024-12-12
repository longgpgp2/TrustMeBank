package com.trustme.dto.request;

import com.trustme.model.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserEditRequest {
    private String accountName;
    private Role role;
    private boolean isDisabled;
}
