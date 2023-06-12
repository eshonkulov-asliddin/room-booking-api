package com.asldev.uz.roombookingapi.service.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@AllArgsConstructor
public class WorkingHours {
    private LocalDate date;
    private LocalDateTime roomOpen;
    private LocalDateTime roomClose;

    public WorkingHours(LocalDate date) {
        this.date = date;
        this.roomOpen = LocalDateTime.of(date, LocalTime.MIN);
        this.roomClose = LocalDateTime.of(date, LocalTime.MAX).truncatedTo(ChronoUnit.SECONDS);
    }

}
