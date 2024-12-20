package com.trustme.enums;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/*
  @Description: This class is for error code and message representations
 */
@Getter
public enum ErrorCode {
    RECEIVER_NOT_FOUND (4000, "Receiver not found!", HttpStatus.BAD_REQUEST),
    INVALID_AMOUNT (4000, "Invalid amount!", HttpStatus.BAD_REQUEST),
    USER_EXISTS(4000, "User existed!", HttpStatus.BAD_REQUEST),
    INVALID_USER (4000, "User not found!", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS (4001, "Invalid username or password!", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(4002, "Username must be at least 8 characters!", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(4003, "Password must be at least 8 characters!", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4004, "User is not found!", HttpStatus.NOT_FOUND),
    UNCATEGORIZED_ERROR (4444, "Uncategorized error !", HttpStatus.BAD_REQUEST),
    USER_NOT_EXIST ( 4005, "User does not exist", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED (4006, "Unauthenticated!", HttpStatus.UNAUTHORIZED),
    NO_CONTENT(2004, "No Content!", HttpStatus.NO_CONTENT),
    UNAUTHORIZED (4007, "You do not have permission", HttpStatus.FORBIDDEN),
    INTERNAL_SERVER_ERROR (5000, "Internal server error!", HttpStatus.INTERNAL_SERVER_ERROR),


            ;

    private int code;
    private String errorMessage;
    private HttpStatus httpStatus;

    ErrorCode(int errorCode, String errorMessage, HttpStatus httpStatus) {
        this.code = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }


}
