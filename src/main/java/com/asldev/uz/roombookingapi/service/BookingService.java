package com.asldev.uz.roombookingapi.service;

import com.asldev.uz.roombookingapi.repository.BookingRepository;
import com.asldev.uz.roombookingapi.repository.RoomRepository;
import com.asldev.uz.roombookingapi.repository.entity.Booking;
import com.asldev.uz.roombookingapi.repository.entity.Room;
import com.asldev.uz.roombookingapi.service.dto.BookingDtoRequest;
import com.asldev.uz.roombookingapi.service.dto.BookingDtoResponse;
import com.asldev.uz.roombookingapi.service.exception.GoneException;
import com.asldev.uz.roombookingapi.service.exception.NotFoundException;
import com.asldev.uz.roombookingapi.service.utils.SuccessMessage;
import com.asldev.uz.roombookingapi.service.utils.Availability;
import com.asldev.uz.roombookingapi.service.utils.ConstantMessages;
import com.asldev.uz.roombookingapi.service.utils.WorkingHours;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    /* Field declarations
     * ==================
     * bookingRepository is used to access the booking-related operations in the database
     * roomRepository is used to access the room-related operations in the database
     * mapper aids in conversion between Entity objects and DTOs
     */
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final ModelMapper mapper;

    /**
     * Constructor for BookingService.
     * Dependencies are automatically injected by Spring.
     *
     * @param bookingRepository Interface for booking-related database operations
     * @param roomRepository Interface for room-related database operations
     */
    @Autowired
    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository){
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.mapper = new ModelMapper();
    }

    /**
     * Retrieves a list of bookings for a specific room.
     *
     * @param id Unique identifier of the room
     * @return A list of bookings (BookingDtoResponse)
     * @throws NotFoundException if no room with the provided id exists
     */
    public List<BookingDtoResponse> findAll(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstantMessages.NOT_FOUND));

        List<Booking> allByRoom = bookingRepository.findAllByRoom(room);

        return allByRoom.stream()
                .map(booking -> mapper.map(booking, BookingDtoResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Fetches the available booking times for a specific room at a specific date.
     *
     * @param roomId Unique identifier of the room
     * @param stringDate The specific date for booking in the form of a string
     * @return List of available times (Availability) for booking
     * @throws NotFoundException if no room with the provided roomId exists
     */
    public List<Availability> getAvailableBookingTimes(Long roomId, String stringDate) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ConstantMessages.NOT_FOUND));

        LocalDate date = toLocalDate(stringDate);
        return findAvailableBookingTimes(room, date);
    }


    /**
     * Finds the available booking times.
     *
     * @param room the Room object
     * @param selectedDate the selected date
     * @return list of time slots (Availability) available for booking
     */
    private List<Availability> findAvailableBookingTimes(Room room, LocalDate selectedDate) {
        WorkingHours workingHours = new WorkingHours(selectedDate);
        List<Booking> bookingList = Optional.ofNullable(
                bookingRepository.findBookingsForRoomInPeriod(
                        room,
                        workingHours.getRoomOpen(),
                        workingHours.getRoomClose())).orElse(Collections.emptyList());

        List<Availability> availabilities = new ArrayList<>();
        for (Booking booking : bookingList) {
            LocalDateTime start = booking.getStart();
            LocalDateTime end = booking.getEnd();
            if ( workingHours.getRoomOpen().isEqual(start) ){
                workingHours.setRoomOpen(end);
                continue;
            }
            if ( workingHours.getRoomOpen().isBefore(start)){

                availabilities.add(new Availability(
                        toStringFormatter(workingHours.getRoomOpen()),
                        toStringFormatter(start)
                ));
                workingHours.setRoomOpen(end);
            }
        }
        availabilities.add(new Availability(
                toStringFormatter(workingHours.getRoomOpen()),
                toStringFormatter(workingHours.getRoomClose())));
        return availabilities;
    }

    /**
     * Books a room given the room id and the details of the room.
     *
     * @param id unique room identifier
     * @param roomDto Data Transfer Object containing the booking details
     * @return message indicating successful booking
     * @throws NotFoundException if room associated with the id not found
     * @throws GoneException if booking time has conflicts or not available
     */
    public SuccessMessage bookRoom(Long id, BookingDtoRequest roomDto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstantMessages.NOT_FOUND));

        LocalDateTime start = toLocalDateTime(roomDto.getStart());
        LocalDateTime end = toLocalDateTime(roomDto.getEnd());
        LocalDate currentDate = start.toLocalDate();

        WorkingHours workingHours = new WorkingHours(currentDate);
        LocalDateTime roomOpen = workingHours.getRoomOpen();
        LocalDateTime roomClose = workingHours.getRoomClose();

        if (isBookingTimeConflict(room, start, end, roomOpen, roomClose)){
            throw new GoneException(ConstantMessages.NOT_AVAILABLE);
        }
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setResident(roomDto.getResident());

        bookingRepository.save(booking);
        return new SuccessMessage(ConstantMessages.SUCCESS);
    }

    private  LocalDateTime toLocalDateTime(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConstantMessages.LOCAL_DATE_TIME_FORMATTER);
        return LocalDateTime.parse(date, formatter);
    }

    /**
     * Helper method to convert a string date to a LocalDateTime object.
     *
     * @param date the string representation of the date
     * @return LocalDateTime object
     */
    private  LocalDate toLocalDate(String date){
        if (date == null){
            return LocalDate.now();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConstantMessages.LOCAL_DATE_FORMATTER);
        return LocalDate.parse(date, formatter);
    }
    private String toStringFormatter(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConstantMessages.LOCAL_DATE_TIME_FORMATTER);
        return dateTime.format(formatter);
    }

    /**
     * Helper method to check if a room booking time overlaps with existing bookings.
     *
     * @param room Room object
     * @param start start booking time
     * @param end end booking time
     * @param roomOpened start working hours of the room
     * @param roomClosed end working hours of the room
     * @return true if there's a time conflict, false otherwise
     */
    private boolean isBookingTimeConflict(Room room, LocalDateTime start, LocalDateTime end,
                                          LocalDateTime roomOpened, LocalDateTime roomClosed) {

        List<Booking> bookingList = bookingRepository.findBookingsForRoomInPeriod(room, roomOpened, roomClosed);

        return bookingList.stream()
                .anyMatch(bookedRoom -> isOverlappingWithExistingBooking(bookedRoom, start, end));
    }

    private boolean isOverlappingWithExistingBooking(Booking bookedRoom, LocalDateTime start, LocalDateTime end) {
        LocalDateTime bookedStart = bookedRoom.getStart();
        LocalDateTime bookedEnd = bookedRoom.getEnd();

        return (start.isEqual(bookedStart) && end.isEqual(bookedEnd)) // new booking is exactly the same as the existing one
                || (start.isEqual(bookedStart) && end.isAfter(bookedEnd)) // new booking starts the same, but ends later
                || (start.isBefore(bookedStart) && end.isEqual(bookedEnd)) // new booking starts earlier, but ends at the same time
                || (start.isBefore(bookedStart) && end.isAfter(bookedEnd)) // new booking starts earlier and ends later
                || (start.isBefore(bookedStart) && end.isAfter(bookedStart)) // new booking starts earlier and ends during the existing booking
                || (start.isAfter(bookedStart) && start.isBefore(bookedEnd)) // new booking starts during the existing booking and ends later
                || (end.isAfter(bookedStart) && end.isBefore(bookedEnd)); // new booking starts and ends during the existing booking
    }

    /**
     * Deletes a booking by id.
     *
     * @param id unique identifier of the booking
     * @throws NotFoundException if booking with the provided id not found
     */
    public void delete(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstantMessages.NOT_FOUND));
        bookingRepository.delete(booking);
    }
}
