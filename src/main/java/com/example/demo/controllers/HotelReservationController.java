package com.example.demo.controllers;

import com.example.demo.entity.HotelReservation;
import com.example.demo.repository.HotelReservationRepository;

import ch.qos.logback.classic.Logger;
import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
@Validated
public class HotelReservationController {

    @Autowired
    private HotelReservationRepository reservationRepository;

    // Obtener todas las reservas
    @GetMapping
    public ResponseEntity<List<HotelReservation>> getAllReservations() {

        List<HotelReservation> reservations = reservationRepository.findAll();
        return ResponseEntity.ok(reservations);
    }

    // Obtener una reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<HotelReservation> getReservationById(@PathVariable Integer id) {
        Optional<HotelReservation> reservation = reservationRepository.findById(id);
        return reservation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear una nueva reserva
    @PostMapping
    public ResponseEntity<HotelReservation> createReservation(@RequestBody HotelReservation reservation) {

        // Verificar disponibilidad
        List<HotelReservation> conflictingReservations = reservationRepository
                .findByRoomNumberAndCheckInDateBeforeAndCheckOutDateAfter(
                        reservation.getRoomNumber(),
                        reservation.getCheckOutDate(),
                        reservation.getCheckInDate());
        if (!conflictingReservations.isEmpty()) {
            // message = "La habitación no está disponible en las fechas seleccionadas"
            return ResponseEntity.badRequest().build();

        }

        HotelReservation savedReservation = reservationRepository.save(reservation);
        return ResponseEntity.ok(savedReservation);
    }

    // Actualizar una reserva existente
    @PutMapping("/{id}")
    public ResponseEntity<HotelReservation> updateReservation(@PathVariable Integer id,
            @RequestBody HotelReservation reservationDetails) {
        Optional<HotelReservation> reservationOptional = reservationRepository.findById(id);

        if (!reservationOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        HotelReservation reservation = reservationOptional.get();
        reservation.setRoomNumber(reservationDetails.getRoomNumber());
        reservation.setGuestName(reservationDetails.getGuestName());
        reservation.setCheckInDate(reservationDetails.getCheckInDate());
        reservation.setCheckOutDate(reservationDetails.getCheckInDate());

        HotelReservation updatedReservation = reservationRepository.save(reservation);
        return ResponseEntity.ok(updatedReservation);
    }

    // Eliminar una reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Integer id) {
        Optional<HotelReservation> reservation = reservationRepository.findById(id);

        if (!reservation.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        reservationRepository.delete(reservation.get());
        return ResponseEntity.noContent().build();
    }

    // Verificar disponibilidad
    @GetMapping("/availability")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam Integer roomNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date checkOutDate) {

        List<HotelReservation> conflictingReservations = reservationRepository
                .findByRoomNumberAndCheckInDateBeforeAndCheckOutDateAfter(roomNumber, checkOutDate, checkInDate);

        boolean isAvailable = conflictingReservations.isEmpty();
        return ResponseEntity.ok(isAvailable);
    }
}
