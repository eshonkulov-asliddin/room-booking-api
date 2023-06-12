package com.asldev.uz.roombookingapi.service;

import com.asldev.uz.roombookingapi.repository.BookingRepository;
import com.asldev.uz.roombookingapi.repository.RoomRepository;
import com.asldev.uz.roombookingapi.repository.entity.Booking;
import com.asldev.uz.roombookingapi.repository.entity.Room;
import com.asldev.uz.roombookingapi.service.dto.BookingDto;
import com.asldev.uz.roombookingapi.service.exception.GoneException;
import com.asldev.uz.roombookingapi.service.exception.NotFoundException;
import com.asldev.uz.roombookingapi.service.exception.SuccessMessage;
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

    public List<BookingDto> findAll(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstantMessages.TOPILMADI));
        List<Booking> allByRoom = bookingRepository.findAllByRoom(room);
        return allByRoom.stream()
                .map(booking -> mapper.map(booking, BookingDto.class))
                .collect(Collectors.toList());
    }

    public List<Availability> getAvailableBookingTimes(Long roomId, LocalDate date) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ConstantMessages.TOPILMADI));

        date = Objects.requireNonNullElseGet(date, LocalDate::now);
        return findAvailableBookingTimes(room, date);
    }

    private List<Availability> findAvailableBookingTimes(Room room, LocalDate selectedDate) {
        WorkingHours workingHours = new WorkingHours(selectedDate);
        LocalDateTime roomOpen = workingHours.getRoomOpen();
        LocalDateTime roomClose = workingHours.getRoomClose();
        List<Booking> bookingList = bookingRepository.findByRoomAndStartGreaterThanEqualAndEndLessThanEqualOrderByStart(room, roomOpen, roomClose);
        List<Availability> availabilities = new ArrayList<>();

        for (Booking booking : bookingList) {
            LocalDateTime start = booking.getStart();
            LocalDateTime end = booking.getEnd();
            if ( roomOpen.isEqual(start) ){
                workingHours.setRoomOpen(end);
                continue;
            }
            if (roomOpen.isBefore(start)){
                availabilities.add(new Availability(workingHours.getRoomOpen(), start));
                workingHours.setRoomOpen(end);
            }
        }
        availabilities.add(new Availability(workingHours.getRoomOpen(), workingHours.getRoomClose()));
        return availabilities;
    }

    public SuccessMessage bookRoom(Long id, BookingDto roomDto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstantMessages.TOPILMADI));

        LocalDateTime start = toLocalDateTime(roomDto.getStart());
        LocalDateTime end = toLocalDateTime(roomDto.getEnd());

        WorkingHours workingHours = new WorkingHours(start.toLocalDate());
        LocalDateTime roomOpen = workingHours.getRoomOpen();
        LocalDateTime roomClose = workingHours.getRoomClose();

        if (isBookingTimeConflict(room, start, end, roomOpen, roomClose)){
            throw new GoneException("uzr, siz tanlagan vaqtda xona band");
        }
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setResident(roomDto.getResident());

        bookingRepository.save(booking);
        return new SuccessMessage(ConstantMessages.BAND_QILINDI);
    }

    private  LocalDateTime toLocalDateTime(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConstantMessages.LOCAL_DATE_TIME_FORMATTER);
        return LocalDateTime.parse(date, formatter);
    }
    private boolean isBookingTimeConflict(Room room, LocalDateTime start, LocalDateTime end, LocalDateTime roomOpened, LocalDateTime roomClosed) {
        List<Booking> bookingList = bookingRepository.findByRoomAndStartGreaterThanEqualAndEndLessThanEqualOrderByStart(room, roomOpened, roomClosed);
        for (Booking bookedRoom : bookingList){
            if (start.isBefore(bookedRoom.getStart()) && end.isBefore(bookedRoom.getStart())) {
                return false;
            }
            else return !start.isAfter(bookedRoom.getEnd()) || !end.isAfter(bookedRoom.getEnd());
        }
        return true;
    }
    public void delete(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstantMessages.TOPILMADI));
        bookingRepository.delete(booking);
    }
}
