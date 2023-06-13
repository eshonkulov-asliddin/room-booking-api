package com.asldev.uz.roombookingapi.controller;

import com.asldev.uz.roombookingapi.enums.RoomType;
import com.asldev.uz.roombookingapi.service.RoomService;
import com.asldev.uz.roombookingapi.service.dto.PageDto;
import com.asldev.uz.roombookingapi.service.dto.RoomDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    private ResponseEntity<PageDto> findAll(@RequestParam(required = false) String search,
                                            @RequestParam(required = false) RoomType type,
                                            @RequestParam(defaultValue = "0") int pageNumber,
                                            @RequestParam(defaultValue = "10") int pageSize){
        PageDto all = roomService.findAll(search, type, pageNumber, pageSize);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<Object> findById(@PathVariable Long id){
        RoomDto roomDto = roomService.findById(id);
        return new ResponseEntity<>(roomDto, HttpStatus.OK);

    }
    @PostMapping("/create")
    private ResponseEntity<RoomDto> create(@RequestBody RoomDto roomDto)
    {
        RoomDto created = roomService.create(roomDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }


    @PutMapping("update/{id}")
    private RoomDto update(@PathVariable Long id,
                           @RequestBody RoomDto roomDto)
    {
        return roomService.update(id, roomDto);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void delete(@PathVariable Long id){
        roomService.delete(id);
    }

}

