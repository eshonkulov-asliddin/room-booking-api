package com.asldev.uz.roombookingapi.service.dto;

import com.asldev.uz.roombookingapi.repository.entity.Room;
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
    private int pageSize;
    private List<RoomDtoResponse> results;
}
