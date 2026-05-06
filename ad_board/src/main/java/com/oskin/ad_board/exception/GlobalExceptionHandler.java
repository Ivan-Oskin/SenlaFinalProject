package com.oskin.ad_board.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private HttpHeaders buildHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CustomExceptionResponse> handlerEntityNotFoundException(EntityNotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        CustomExceptionResponse customExceptionResponse = new CustomExceptionResponse(status.value(), e.getMessage());
        return new ResponseEntity<>(customExceptionResponse, buildHeaders(), status);
    }

    @ExceptionHandler(IdMatchException.class)
    public ResponseEntity<CustomExceptionResponse> handlerIdMatchException(IdMatchException e) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        CustomExceptionResponse customExceptionResponse = new CustomExceptionResponse(status.value(), e.getMessage());
        return new ResponseEntity<>(customExceptionResponse, buildHeaders(), status);
    }

    @ExceptionHandler(StatusNoValidException.class)
    public ResponseEntity<CustomExceptionResponse> handlerStatusNoValidException(StatusNoValidException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomExceptionResponse customExceptionResponse = new CustomExceptionResponse(status.value(), e.getMessage());
        return new ResponseEntity<>(customExceptionResponse, buildHeaders(), status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomExceptionResponse> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String messages = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        CustomExceptionResponse customExceptionResponse = new CustomExceptionResponse(status.value(), messages);
        return new ResponseEntity<>(customExceptionResponse, buildHeaders(), status);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<CustomExceptionResponse> handlerEntityExistsExceptionException(EntityExistsException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        CustomExceptionResponse customExceptionResponse = new CustomExceptionResponse(status.value(), e.getMessage());
        return new ResponseEntity<>(customExceptionResponse, buildHeaders(), status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomExceptionResponse> handlerGlobalException(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        CustomExceptionResponse customExceptionResponse = new CustomExceptionResponse(status.value(), "An unexpected error has occurred");
        return new ResponseEntity<>(customExceptionResponse, buildHeaders(), status);
    }
}
