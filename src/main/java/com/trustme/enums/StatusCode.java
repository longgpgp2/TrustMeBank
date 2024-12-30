package com.trustme.enums;

import lombok.Getter;

@Getter
public enum StatusCode {
    OK("OK!", 200),
    CREATED( "Created!", 201),
    ACCEPTED( "Accepted!", 202),
    NO_CONTENT("No content!", 204);

    private String statusMessage;
    private int httpStatus;

    StatusCode( String statusMessage, int httpStatus) {
        this.statusMessage = statusMessage;
        this.httpStatus = httpStatus;
    }
}
