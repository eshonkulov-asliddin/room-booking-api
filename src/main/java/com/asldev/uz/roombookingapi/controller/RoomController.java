package com.asldev.uz.roombookingapi.controller;

import com.asldev.uz.roombookingapi.enums.RoomType;
import com.asldev.uz.roombookingapi.service.RoomService;
import com.asldev.uz.roombookingapi.service.dto.PageDto;
import com.asldev.uz.roombookingapi.service.dto.RoomDtoRequest;
import com.asldev.uz.roombookingapi.service.dto.RoomDtoResponse;
import com.asldev.uz.roombookingapi.service.dto.RoomDtoUpdate;
import com.asldev.uz.roombookingapi.service.utils.ConstantMessages;
import com.asldev.uz.roombookingapi.service.utils.SuccessMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/rooms")
@Tag(name = "room", description = "a room API")
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(summary = "Get all rooms from database")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping
    private ResponseEntity<PageDto> findAll(@Parameter(description = "Key for fetching objects by") @RequestParam(required = false) String search,
                                            @Parameter(description = "Available room type") @RequestParam(required = false) RoomType type,
                                            @Parameter(description = "Current page") @RequestParam(defaultValue = "0") int pageNumber,
                                            @Parameter(description = "Number of objects") @RequestParam(defaultValue = "10") int pageSize){
        PageDto all = roomService.findAll(search, type, pageNumber, pageSize);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @Operation(summary = "Get room by id from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Room with given id is not found")
    })
    @GetMapping("/{id}")
    private ResponseEntity<RoomDtoResponse> findById(@Parameter(description = "ID of room that need to be fetched") @PathVariable Long id){
        RoomDtoResponse roomDtoResponse = roomService.findById(id);
        return new ResponseEntity<>(roomDtoResponse, HttpStatus.OK);

    }
    @Operation(summary = "Create a room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Given object is not valid")
    })
    @PostMapping
    private ResponseEntity<RoomDtoResponse> create(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Object for creating room")
                                                      @RequestBody RoomDtoRequest createRequest) {
        RoomDtoResponse created = roomService.create(createRequest);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }


    @Operation(summary = "Update a room by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description ="Room with given id is not found")
    })
    @PutMapping("/{id}")
    private ResponseEntity<RoomDtoResponse> update(@Parameter(description = "ID of room that need to be updated") @PathVariable Long id,
                                                  @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Object with updated content")
                                                    @RequestBody RoomDtoUpdate updateRequest) {
        RoomDtoResponse update = roomService.update(id, updateRequest);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @Operation(summary = "Delete a room by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description ="Room with given id is not found")
    })
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private ResponseEntity<SuccessMessage> delete(@Parameter(description = "ID of room that need to be deleted") @PathVariable Long id){
        roomService.delete(id);
        return new ResponseEntity<>(new SuccessMessage(ConstantMessages.DELETE), HttpStatus.NO_CONTENT);
    }

}

