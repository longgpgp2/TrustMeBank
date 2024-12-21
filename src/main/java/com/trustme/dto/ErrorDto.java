package com.trustme.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class ErrorDto {
    HttpStatusCode errorCode;
    String message;
}
