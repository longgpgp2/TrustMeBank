package com.trustme.exception;

import com.trustme.enums.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * @description This class is for resource not found exception
 * @static errorCode
 * @attribute resource
 */

@Getter
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class NotFoundException extends RuntimeException{
    static ErrorCode errorCode = ErrorCode.NOT_FOUND;
    String resource;
    /**
     * @constructor
     * @param resource
     * @examples User, Transfer, ...
     */
    public NotFoundException(String resource){
        super(resource + errorCode.getErrorMessage());
        this.resource = resource;
    }
}
