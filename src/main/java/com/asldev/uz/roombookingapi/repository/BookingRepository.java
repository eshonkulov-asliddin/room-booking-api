package com.asldev.uz.roombookingapi.repository;

import com.asldev.uz.roombookingapi.repository.entity.Booking;
import com.asldev.uz.roombookingapi.repository.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByRoom(Room room);

    @Query("SELECT b FROM Booking b WHERE b.room = :room AND b.start >= :start AND b.end <= :end ORDER BY b.start")
    List<Booking> findBookingsForRoomInPeriod(@Param("room") Room room, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


}
