package com.trustme.dto;

import org.springframework.http.HttpStatusCode;

public class ErrorDto {
    HttpStatusCode errorCode;
    String message;
}
