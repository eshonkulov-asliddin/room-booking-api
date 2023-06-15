package com.asldev.uz.roombookingapi.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ArgumentIsNotValidException extends RuntimeException {
    public ArgumentIsNotValidException(String msg) {
        super(msg);
    }
}
