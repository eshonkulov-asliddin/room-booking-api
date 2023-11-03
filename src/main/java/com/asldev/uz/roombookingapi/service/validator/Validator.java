package com.asldev.uz.roombookingapi.service.validator;

public interface Validator<T> {
    void validate(T t);
}