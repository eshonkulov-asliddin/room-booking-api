package com.asldev.uz.roombookingapi.repository;

import com.asldev.uz.roombookingapi.enums.RoomType;
import com.asldev.uz.roombookingapi.repository.entity.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Slice<Room> findByNameAndType(String name,RoomType roomType, Pageable pageable);
    Slice<Room> findByName(String name, Pageable pageable);
    Slice<Room> findByType(RoomType roomType, Pageable pageable);
}
