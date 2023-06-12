package com.asldev.uz.roombookingapi.service.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Availability {
    private LocalDateTime start;
    private LocalDateTime end;
}
