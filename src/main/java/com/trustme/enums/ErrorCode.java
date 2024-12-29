package com.trustme.enums;

import lombok.Getter;

/*
  @Description: This class is for error code and message representations
 */
@Getter
public enum ErrorCode {
    RECEIVER_NOT_FOUND("Receiver not found!", 400),
    INVALID_AMOUNT("Invalid amount!", 400),
    USER_EXISTS("User existed!", 400),
    INVALID_USER( "User not found!", 400),
    INVALID_CREDENTIALS("Invalid username or password!", 400),
    INVALID_USERNAME("Username must be at least 8 characters!", 400),
    INVALID_PASSWORD( "Password must be at least 8 characters!", 400),
    USER_NOT_FOUND("User is not found!", 404),
    UNCATEGORIZED_ERROR("Uncategorized error!", 400),
    USER_NOT_EXIST("User does not exist", 400),
    UNAUTHENTICATED( "Unauthenticated!", 401),
    NO_CONTENT("No Content!", 204),
    UNAUTHORIZED("You do not have permission", 403),
    INTERNAL_SERVER_ERROR( "Internal server error!", 500);

    private String errorMessage;
    private int httpStatus; // Change HttpStatus to int

    ErrorCode(String errorMessage, int httpStatus) {
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
