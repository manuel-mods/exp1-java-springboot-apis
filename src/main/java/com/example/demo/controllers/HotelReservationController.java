package com.example.demo.controllers;

import com.example.demo.entity.HotelReservation;
import com.example.demo.repository.HotelReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
public class HotelReservationController {

    @Autowired
    private HotelReservationRepository reservationRepository;

    // Obtener todas las reservas con enlaces HATEOAS
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<HotelReservation>>> getAllReservations() {
        List<EntityModel<HotelReservation>> reservations = ((List<HotelReservation>) reservationRepository.findAll())
                .stream()
                .map(reservation -> toModel(reservation))
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
}
