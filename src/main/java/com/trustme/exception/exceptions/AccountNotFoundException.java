package com.trustme.exception.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AccountNotFoundException extends Exception{
    public AccountNotFoundException(String message) {
        super(message);
    }

}
