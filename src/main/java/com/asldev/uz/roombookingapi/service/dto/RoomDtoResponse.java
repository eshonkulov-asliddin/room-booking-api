package com.asldev.uz.roombookingapi.service.dto;

import com.asldev.uz.roombookingapi.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomDtoResponse {
    private Long id;
    private String name;
    private RoomType type;
    private int capacity;
}
