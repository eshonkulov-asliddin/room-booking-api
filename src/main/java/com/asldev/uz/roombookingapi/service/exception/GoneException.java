package com.asldev.uz.roombookingapi.service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoneException extends RuntimeException {
    public GoneException(String message) {
        super(message);
    }

}
