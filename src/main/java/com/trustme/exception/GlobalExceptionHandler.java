package com.trustme.exception;

import com.trustme.dto.response.Response;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Response> handlingRuntimeException (RuntimeException exception){
        Response errorBody = Response.builder()
                .code(HttpStatusCode.valueOf(400))
                .message("Something wrong in runtime!")
                .build();

        return ResponseEntity.badRequest().body(errorBody);
    }
    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<Response> handlingUsernameNotFoundException (UsernameNotFoundException exception){
        Response errorBody = Response.builder()
                .code(HttpStatusCode.valueOf(400))
                .message("Username not found!")
                .build();

        return ResponseEntity.badRequest().body(errorBody);
    }
}
