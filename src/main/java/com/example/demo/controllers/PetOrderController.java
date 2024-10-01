package com.example.demo.controllers;

import com.example.demo.entity.PetOrder;
import com.example.demo.repository.PetOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class PetOrderController {

    @Autowired
    private PetOrderRepository orderRepository;

    // Obtener todas las órdenes con enlaces HATEOAS
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PetOrder>>> getAllOrders() {
        List<EntityModel<PetOrder>> orders = ((List<PetOrder>) orderRepository.findAll())
                .stream()
                .map(order -> toModel(order))
                .collect(Collectors.toList());

        // Agregar enlace a la colección de órdenes
        CollectionModel<EntityModel<PetOrder>> collectionModel = CollectionModel.of(orders);
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PetOrderController.class).getAllOrders()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    // Obtener una orden por ID con enlaces HATEOAS
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PetOrder>> getOrderById(@PathVariable Integer id) {
        Optional<PetOrder> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            return ResponseEntity.ok(toModel(orderOpt.get()));
        }
        return ResponseEntity.notFound().build();
    }

    // Agregar una nueva orden con enlaces HATEOAS
    @PostMapping
    public ResponseEntity<EntityModel<PetOrder>> addOrder(@RequestBody PetOrder newOrder) {
        PetOrder order = orderRepository.save(newOrder);
        return ResponseEntity.ok(toModel(order));
    }

    // Actualizar una orden existente con enlaces HATEOAS
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PetOrder>> updateOrder(@PathVariable Integer id, @RequestBody PetOrder updatedOrder) {
        Optional<PetOrder> existingOrderOpt = orderRepository.findById(id);
        if (existingOrderOpt.isPresent()) {
            updatedOrder.setId(id);
            PetOrder updated = orderRepository.save(updatedOrder);
            return ResponseEntity.ok(toModel(updated));
        }
        return ResponseEntity.notFound().build();
    }

    // Eliminar una orden con enlaces HATEOAS
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Método auxiliar para convertir una entidad en EntityModel con enlaces HATEOAS
    private EntityModel<PetOrder> toModel(PetOrder order) {
        EntityModel<PetOrder> resource = EntityModel.of(order);

        // Enlace a sí mismo
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PetOrderController.class).getOrderById(order.getId())).withSelfRel();
        resource.add(selfLink);

        // Enlace a la colección de órdenes
        Link ordersLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PetOrderController.class).getAllOrders()).withRel("orders");
        resource.add(ordersLink);

        return resource;
    }
}