package com.trustme.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StatusCode {
    OK(2000, "OK!", HttpStatus.OK),
    CREATED(2001, "Created!", HttpStatus.CREATED),
    ACCEPTED(2002, "Accepted!", HttpStatus.ACCEPTED),
    NO_CONTENT(2004, "No content!", HttpStatus.NO_CONTENT),

    ;

    private int code;
    private String statusMessage;
    private HttpStatus httpStatus;

    StatusCode(int statusCode, String statusMessage, HttpStatus httpStatus) {
        this.code = statusCode;
        this.statusMessage = statusMessage;
        this.httpStatus = httpStatus;
    }
}
