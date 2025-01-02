package com.trustme.exception;

import com.trustme.dto.response.Response;
import com.trustme.exception.exceptions.ResourceNotAvailableException;
import com.trustme.exception.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * GlobalExceptionHandler handles exceptions globally across the application.
 * Uses @ControllerAdvice to provide centralized exception handling.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles generic RuntimeExceptions that are not explicitly handled by other methods.
     *
     * @param exception the RuntimeException to handle
     * @return a ResponseEntity containing a generic error response with a 400 status code
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Response> handlingRuntimeException(RuntimeException exception) {
        Response errorBody = Response.builder()
                .code(400)
                .message(exception.getMessage() != null ? exception.getMessage() : "Something wrong in runtime!")
                .build();

        return ResponseEntity.badRequest().body(errorBody);
    }

    /**
     * Handles UsernameNotFoundException, typically thrown by Spring Security when a user is not found.
     *
     * @param exception the UsernameNotFoundException to handle
     * @return a ResponseEntity containing an error response with a message and a 400 status code
     */
    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<Response> handlingUsernameNotFoundException(UsernameNotFoundException exception) {
        Response errorBody = Response.builder()
                .code(400)
                .message(exception.getMessage())
                .build();

        return ResponseEntity.badRequest().body(errorBody);
    }


    /**
     * Handles IllegalArgumentException, typically thrown when there is a mismatch between the argument types.
     *
     * @param exception the IllegalArgumentException to handle
     * @return a ResponseEntity containing an error response with a message and a 400 status code
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Response> handlingIllegalArgumentException(IllegalArgumentException exception) {
        Response errorBody = Response.builder()
                .code(400)
                .message("Invalid request datatype: " + exception.getMessage())
                .build();

        return ResponseEntity.badRequest().body(errorBody);
    }

    /**
     * Handles ResourceNotFoundException, typically thrown when a requested resource does not exist.
     *
     * @param exception the ResourceNotFoundException to handle
     * @return a ResponseEntity containing an error response with a message and a 400 status code
     */
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Response> handlingResourceNotFoundException(ResourceNotFoundException exception) {
        Response errorBody = Response.builder()
                .code(400)
                .message(exception.getMessage())
                .build();

        return ResponseEntity.badRequest().body(errorBody);
    }

    /**
     * Handles ResourceNotAvailableException, typically thrown when a request try to create a new
     * resource that has already existed.
     *
     * @param exception the ResourceNotAvailableException to handle
     * @return a ResponseEntity containing an error response with a message and a 400 status code
     */
    @ExceptionHandler(value = ResourceNotAvailableException.class)
    public ResponseEntity<Response> handlingResourceNotAvailableException(ResourceNotAvailableException exception) {
        Response errorBody = Response.builder()
                .code(400)
                .message(exception.getMessage())
                .build();

        return ResponseEntity.badRequest().body(errorBody);
    }

    /**
     * Handles HttpMessageNotReadableException, typically thrown when the request body cannot be parsed.
     *
     * @param exception the HttpMessageNotReadableException to handle
     * @return a ResponseEntity containing an error response with a message and a 400 status code
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<Response> handlingRequestParsingException(HttpMessageNotReadableException exception) {
        Response errorBody = Response.builder()
                .code(400)
                .message("Error parsing the request!: "+exception.getMessage())
                .build();

        return ResponseEntity.badRequest().body(errorBody);
    }

    /**
     * Handles BadCredentialsException, typically thrown when the client enters invalid passwords.
     *
     * @param exception the BadCredentialsException to handle
     * @return a ResponseEntity containing an error response with a message and a 400 status code
     */
    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<Response> handlingBadCredentialsException(BadCredentialsException exception) {
        Response errorBody = Response.builder()
                .code(400)
                .message(exception.getMessage())
                .build();
        return ResponseEntity.badRequest().body(errorBody);
    }




}
