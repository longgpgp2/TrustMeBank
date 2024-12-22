package com.trustme.exception.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UsernameNotAvailableException extends Exception{
    public UsernameNotAvailableException(String message) {
        super(message);
    }

}
