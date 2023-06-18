package com.asldev.uz.roombookingapi.service.dto;

import com.asldev.uz.roombookingapi.repository.entity.Resident;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingDtoResponse {
    private Long id;
    private String start;
    private String end;
    private Resident resident;


    public BookingDtoResponse(String start, String end, Resident resident){
        this.start = start;
        this.end = end;
        this.resident = resident;
    }
}
