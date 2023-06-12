package com.asldev.uz.roombookingapi.repository.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "BOOKINGS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "start_time")
    private LocalDateTime start;

    @Column(nullable = false, name = "end_time")
    private LocalDateTime end;

    private Resident resident;

    @ManyToOne
    @JoinColumn(name = "ROOM_ID")
    private Room room;
}
