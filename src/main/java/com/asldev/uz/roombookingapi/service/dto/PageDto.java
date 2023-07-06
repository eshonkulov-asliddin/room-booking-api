package com.asldev.uz.roombookingapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageDto {
    private int page;
    private long count;
    private int page_size;
    private List<RoomDtoResponse> results;
}
