package com.example.demo.repository;

import com.example.demo.entity.HotelReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface HotelReservationRepository extends JpaRepository<HotelReservation, Integer> {

    List<HotelReservation> findByRoomNumberAndCheckInDateBeforeAndCheckOutDateAfter(
            Integer roomNumber, Date checkOutDate, Date checkInDate);
}
