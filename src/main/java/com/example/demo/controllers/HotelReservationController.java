package com.example.demo.controllers;

import com.example.demo.entity.HotelReservation;
import com.example.demo.entity.Room;
import com.example.demo.repository.HotelReservationRepository;
import com.example.demo.repository.RoomRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
public class HotelReservationController {

    @Autowired
    private HotelReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    // Obtener todas las reservas con enlaces HATEOAS
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<HotelReservation>>> getAllReservations() {
        List<EntityModel<HotelReservation>> reservations = ((List<HotelReservation>) reservationRepository.findAll())
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        // Agregar enlace a la colección de reservas
        CollectionModel<EntityModel<HotelReservation>> collectionModel = CollectionModel.of(reservations);
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(HotelReservationController.class).getAllReservations()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    // Obtener una reserva por ID con enlaces HATEOAS
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<HotelReservation>> getReservationById(@PathVariable Integer id) {
        Optional<HotelReservation> reservationOpt = reservationRepository.findById(id);
        if (reservationOpt.isPresent()) {
            return ResponseEntity.ok(toModel(reservationOpt.get()));
        }
        return ResponseEntity.notFound().build();
    }

    // Agregar una nueva reserva con enlaces HATEOAS
    @PostMapping
    public ResponseEntity<EntityModel<HotelReservation>> addReservation(@RequestBody HotelReservation newReservation) {
        // Validar si la habitación existe
        Room room = roomRepository.findByRoomNumber(newReservation.getRoomNumber());
        
        if (room == null) {
            return ResponseEntity.badRequest().body(EntityModel.of(newReservation).add(Link.of("La habitación no existe")));
        }

        // Verificar si la habitación está disponible en las fechas propuestas
        List<HotelReservation> reservations = reservationRepository.findByRoomNumberAndCheckInDateBeforeAndCheckOutDateAfter(
                newReservation.getRoomNumber(), newReservation.getCheckOutDate(), newReservation.getCheckInDate());
        if (!reservations.isEmpty()) {
            return ResponseEntity.badRequest().body(EntityModel.of(newReservation).add(Link.of("La habitación ya está reservada en las fechas propuestas")));
        }

        // Guardar la reserva si la habitación existe y está disponible
        HotelReservation reservation = reservationRepository.save(newReservation);
        return ResponseEntity.ok(toModel(reservation));
    }

    // Actualizar una reserva existente con enlaces HATEOAS
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<HotelReservation>> updateReservation(@PathVariable Integer id, @RequestBody HotelReservation updatedReservation) {
        Optional<HotelReservation> existingReservationOpt = reservationRepository.findById(id);
        if (existingReservationOpt.isPresent()) {
            updatedReservation.setId(id);
            HotelReservation updated = reservationRepository.save(updatedReservation);
            return ResponseEntity.ok(toModel(updated));
        }
        return ResponseEntity.notFound().build();
    }

    // Eliminar una reserva con enlaces HATEOAS
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Integer id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/availability")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam Integer roomNumber,
            @RequestParam Date checkInDate,
            @RequestParam Date checkOutDate) {
        try {
            Room room = roomRepository.findByRoomNumber(roomNumber);
            if (room == null) {
                return ResponseEntity.badRequest().body(false);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }
        try {
        List<HotelReservation> conflictingReservations = reservationRepository
                .findByRoomNumberAndCheckInDateBeforeAndCheckOutDateAfter(roomNumber, checkOutDate, checkInDate);
                boolean isAvailable = conflictingReservations.isEmpty();
                return ResponseEntity.ok(isAvailable);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }

    }


    @GetMapping("/rooms-availability")
    public ResponseEntity<RoomAvailabilityResponse> getRoomsAvailability(
            @RequestParam Date checkInDate,
            @RequestParam Date checkOutDate) {

        // Obtener todas las habitaciones
        List<Room> allRooms = (List<Room>) roomRepository.findAll();

        // Separar habitaciones disponibles y no disponibles
        List<Room> availableRooms = allRooms.stream()
                .filter(room -> {
                    List<HotelReservation> reservations = reservationRepository
                            .findByRoomNumberAndCheckInDateBeforeAndCheckOutDateAfter(
                                    room.getRoomNumber(), checkOutDate, checkInDate);
                    return reservations.isEmpty();
                })
                .collect(Collectors.toList());

        List<Room> unavailableRooms = allRooms.stream()
                .filter(room -> !availableRooms.contains(room))
                .collect(Collectors.toList());

        RoomAvailabilityResponse response = new RoomAvailabilityResponse(availableRooms, unavailableRooms);

        return ResponseEntity.ok(response);
    }

    // Método auxiliar para convertir una entidad en EntityModel con enlaces HATEOAS
    private EntityModel<HotelReservation> toModel(HotelReservation reservation) {
        EntityModel<HotelReservation> resource = EntityModel.of(reservation);

        // Enlace a sí mismo
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(HotelReservationController.class).getReservationById(reservation.getId())).withSelfRel();
        resource.add(selfLink);

        // Enlace a la colección de reservas
        Link reservationsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(HotelReservationController.class).getAllReservations()).withRel("reservations");
        resource.add(reservationsLink);

        return resource;
    }

    public static class RoomAvailabilityResponse {
        private List<Room> availableRooms;
        private List<Room> unavailableRooms;

        public RoomAvailabilityResponse(List<Room> availableRooms, List<Room> unavailableRooms) {
            this.availableRooms = availableRooms;
            this.unavailableRooms = unavailableRooms;
        }

        public List<Room> getAvailableRooms() {
            return availableRooms;
        }

        public void setAvailableRooms(List<Room> availableRooms) {
            this.availableRooms = availableRooms;
        }

        public List<Room> getUnavailableRooms() {
            return unavailableRooms;
        }

        public void setUnavailableRooms(List<Room> unavailableRooms) {
            this.unavailableRooms = unavailableRooms;
        }
    }
}
