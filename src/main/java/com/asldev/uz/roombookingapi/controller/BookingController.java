package com.asldev.uz.roombookingapi.controller;

import com.asldev.uz.roombookingapi.service.BookingService;
import com.asldev.uz.roombookingapi.service.dto.BookingDto;
import com.asldev.uz.roombookingapi.service.exception.SuccessMessage;
import com.asldev.uz.roombookingapi.service.utils.Availability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/rooms")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }

    @GetMapping("/{roomId}/availability")
    private ResponseEntity<List<Availability>> getAvailableBookingTimes(@PathVariable Long roomId,
                                                                       @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date)
    {
        List<Availability> availableBookingTimes = bookingService.getAvailableBookingTimes(roomId, date);
        return new ResponseEntity<>(availableBookingTimes, HttpStatus.OK);
    }

    @PostMapping("/{id}/book")
    private ResponseEntity<SuccessMessage> bookRoom(@PathVariable Long id,
                                                    @RequestBody BookingDto bookingDto) {
        SuccessMessage successMessage = bookingService.bookRoom(id, bookingDto);
        return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/bookings")
    private ResponseEntity<List<BookingDto>> findAll(@PathVariable Long id) {
        List<BookingDto> all = bookingService.findAll(id);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @DeleteMapping("/bookings/{id}")
    private void delete(@PathVariable Long id){
        bookingService.delete(id);
    }
}
