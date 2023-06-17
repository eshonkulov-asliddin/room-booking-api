package com.asldev.uz.roombookingapi.service.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Availability {
    private LocalDateTime start;
    private LocalDateTime end;
}
