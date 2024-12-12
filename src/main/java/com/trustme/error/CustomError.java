package com.trustme.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
@RequiredArgsConstructor
public class CustomError<T>{
    HttpStatusCode errorCode;
    String message;
    T error;
}
