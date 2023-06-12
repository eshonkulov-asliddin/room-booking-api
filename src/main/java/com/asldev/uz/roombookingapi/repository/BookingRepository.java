package com.asldev.uz.roombookingapi.repository;

import com.asldev.uz.roombookingapi.repository.entity.Booking;
import com.asldev.uz.roombookingapi.repository.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByRoom(Room room);

    List<Booking> findByRoomOrderByStart(Room room);

    List<Booking> findByRoomAndStartGreaterThanEqualAndEndLessThanEqualOrderByStart(Room room, LocalDateTime start, LocalDateTime end);

}
