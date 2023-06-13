package com.asldev.uz.roombookingapi.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleException(NotFoundException exception){
        return new ResponseEntity<>(new ErrorMessage(exception.getMessage()), HttpStatus.NOT_FOUND);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(GoneException.class)
    public ResponseEntity<Object> handleException(GoneException exception){
        return new ResponseEntity<>(new ErrorMessage(exception.getMessage()), HttpStatus.GONE);
    }
}
