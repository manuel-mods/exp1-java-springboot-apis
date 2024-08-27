package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dtos.HotelReservation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@RestController
public class HotelReservationController {
    ArrayList<HotelReservation> reservations = new ArrayList<>();

    HotelReservationController() {
        reservations.add(new HotelReservation(1, 101, "Carlos Muñoz", "2024-09-01", "2024-09-05"));
        reservations.add(new HotelReservation(2, 102, "Ana Pérez", "2024-09-05", "2024-09-10"));
        reservations.add(new HotelReservation(3, 103, "Pedro González", "2024-09-10", "2024-09-15"));
        reservations.add(new HotelReservation(4, 104, "Luis Rodríguez", "2024-09-15", "2024-09-20"));
        reservations.add(new HotelReservation(5, 105, "Josefa Fernández", "2024-09-20", "2024-09-25"));
        reservations.add(new HotelReservation(6, 106, "Matías Rojas", "2024-09-25", "2024-09-30"));
        reservations.add(new HotelReservation(7, 107, "Sebastián Díaz", "2024-10-01", "2024-10-05"));
        reservations.add(new HotelReservation(8, 108, "Isidora Valdés", "2024-10-05", "2024-10-10"));
    }

    @GetMapping("/reservations")
    public ResponseEntity<ArrayList<HotelReservation>> getReservations() {
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<HotelReservation> getReservationById(@PathVariable("id") int reservationId) {
        for (HotelReservation reservation : reservations) {
            if (reservation.getId() == reservationId) {
                return ResponseEntity.ok(reservation);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/availability")
    public ResponseEntity<Boolean> checkAvailability(@RequestParam int roomNumber, @RequestParam String checkInDate, @RequestParam String checkOutDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkIn = LocalDate.parse(checkInDate, formatter);
        LocalDate checkOut = LocalDate.parse(checkOutDate, formatter);

        for (HotelReservation reservation : reservations) {
            LocalDate reservedCheckIn = LocalDate.parse(reservation.getCheckInDate(), formatter);
            LocalDate reservedCheckOut = LocalDate.parse(reservation.getCheckOutDate(), formatter);

            if (reservation.getRoomNumber() == roomNumber &&
                (checkIn.isBefore(reservedCheckOut) && checkOut.isAfter(reservedCheckIn))) {
                return ResponseEntity.ok(false); // No disponible
            }
        }
        return ResponseEntity.ok(true); // Disponible
    }

    @PostMapping("/reservations")
    public ResponseEntity<HotelReservation> addReservation(@RequestBody HotelReservation reservation) {
        reservations.add(reservation);
        return ResponseEntity.ok(reservation);
    }
}
