package com.asldev.uz.roombookingapi.controller;

import com.asldev.uz.roombookingapi.service.BookingService;
import com.asldev.uz.roombookingapi.service.dto.BookingDtoRequest;
import com.asldev.uz.roombookingapi.service.dto.BookingDtoResponse;
import com.asldev.uz.roombookingapi.service.utils.SuccessMessage;
import com.asldev.uz.roombookingapi.service.utils.Availability;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/rooms")
@Tag(name = "booking", description = "a booking API")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }

    @Operation(summary = "Get available booking times of room by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Room with given id is not found")
    })
    @GetMapping("/{id}/availability")
    private ResponseEntity<List<Availability>> getAvailableBookingTimes(@Parameter(description = "ID of room that need to fetched", required = true) @PathVariable Long id,
                                                                       @RequestParam(name = "date", required = false) String date)
    {
        List<Availability> availableBookingTimes = bookingService.getAvailableBookingTimes(id, date);
        return new ResponseEntity<>(availableBookingTimes, HttpStatus.OK);
    }

    @Operation(summary = "Book a room with given id and booking obj")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Room with given id is not found")
    })
    @PostMapping("/{id}/book/")
    private ResponseEntity<SuccessMessage> bookRoom(@PathVariable Long id,
                                                    @RequestBody BookingDtoRequest createRequest) {
        SuccessMessage successMessage = bookingService.bookRoom(id, createRequest);
        return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
    }
    @Operation(summary = "Get all bookings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Room with given id is not found")
    })
    @GetMapping("/{id}/bookings")
    private ResponseEntity<List<BookingDtoResponse>> findAll(@Parameter(description = "ID of room that is associated with booking", required = true) @PathVariable Long id) {
        List<BookingDtoResponse> all = bookingService.findAll(id);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @Operation(summary = "Delete booking with given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Room with given id is not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/bookings/{id}")
    private void delete(@Parameter(description = "ID of booking that need to be deleted", required = true) @PathVariable Long id){
        bookingService.delete(id);
    }
}
