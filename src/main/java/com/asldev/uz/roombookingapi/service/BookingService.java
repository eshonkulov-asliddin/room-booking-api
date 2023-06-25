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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final ModelMapper mapper;

    @Autowired
    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository){
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.mapper = new ModelMapper();
    }

    public List<BookingDtoResponse> findAll(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstantMessages.NOT_FOUND));
        List<Booking> allByRoom = bookingRepository.findAllByRoom(room);
        return allByRoom.stream()
                .map(booking -> mapper.map(booking, BookingDtoResponse.class))
                .collect(Collectors.toList());
    }

    public List<Availability> getAvailableBookingTimes(Long roomId, String stringDate) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ConstantMessages.NOT_FOUND));

        LocalDate date = toLocalDate(stringDate);
        return findAvailableBookingTimes(room, date);
    }

    private List<Availability> findAvailableBookingTimes(Room room, LocalDate selectedDate) {
        WorkingHours workingHours = new WorkingHours(selectedDate);
        List<Booking> bookingList = bookingRepository.findByRoomAndStartGreaterThanEqualAndEndLessThanEqualOrderByStart(room, workingHours.getRoomOpen(), workingHours.getRoomClose());
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
                        localDateTimeFormatter(workingHours.getRoomOpen()),
                        localDateTimeFormatter(start)
                ));
                workingHours.setRoomOpen(end);
            }
        }
        availabilities.add(new Availability(
                localDateTimeFormatter(workingHours.getRoomOpen()),
                localDateTimeFormatter(workingHours.getRoomClose())));

        return availabilities;
    }

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
    private  LocalDate toLocalDate(String date){
        if (date == null){
            return LocalDate.now();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date, formatter);
    }
    private String localDateTimeFormatter(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }
    private boolean isBookingTimeConflict(Room room, LocalDateTime start, LocalDateTime end, LocalDateTime roomOpened, LocalDateTime roomClosed) {
        List<Booking> bookingList = bookingRepository.findByRoomAndStartGreaterThanEqualAndEndLessThanEqualOrderByStart(room, roomOpened, roomClosed);
        if (bookingList.isEmpty()){
            return false;
        }
        for (Booking bookedRoom : bookingList){
            if (    (start.isEqual(bookedRoom.getStart()) && end.isEqual(bookedRoom.getEnd())) ||
                    (start.isEqual(bookedRoom.getStart()) && end.isAfter(bookedRoom.getEnd())) ||
                    (start.isBefore(bookedRoom.getStart()) && end.isEqual(bookedRoom.getEnd())) ||
                    (start.isBefore(bookedRoom.getStart()) && end.isAfter(bookedRoom.getEnd())) ||
                    (start.isBefore(bookedRoom.getStart()) && end.isAfter(bookedRoom.getStart())) ||
                    (start.isAfter(bookedRoom.getStart()) && start.isBefore(bookedRoom.getEnd())) ||
                    (end.isAfter(bookedRoom.getStart()) && end.isBefore(bookedRoom.getEnd()))  )
            {
                return true;
            }
        }
        return false;
    }
    public void delete(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstantMessages.NOT_FOUND));
        bookingRepository.delete(booking);
    }
}
