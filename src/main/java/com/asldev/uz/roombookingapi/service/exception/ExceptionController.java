package com.asldev.uz.roombookingapi.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleException(NotFoundException exception){
        return new ResponseEntity<>(new ErrorMessage(exception.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(GoneException.class)
    public ResponseEntity<Object> handleException(GoneException exception){
        return new ResponseEntity<>(new ErrorMessage(exception.getMessage()), HttpStatus.GONE);
    }
}
