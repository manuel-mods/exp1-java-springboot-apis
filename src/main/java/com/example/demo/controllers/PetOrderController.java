package com.example.demo.controllers;

import com.example.demo.entity.PetOrder;
import com.example.demo.repository.PetOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@Validated
public class PetOrderController {

    @Autowired
    private PetOrderRepository orderRepository;

    // Obtener todas las órdenes
    @GetMapping
    public ResponseEntity<List<PetOrder>> getAllOrders() {
        List<PetOrder> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    // Obtener una orden por ID
    @GetMapping("/{id}")
    public ResponseEntity<PetOrder> getOrderById(@PathVariable Integer id) {
        Optional<PetOrder> order = orderRepository.findById(id);
        return order.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear una nueva orden
    @PostMapping
    public ResponseEntity<PetOrder> createOrder(@RequestBody PetOrder order) {
        PetOrder savedOrder = orderRepository.save(order);
        return ResponseEntity.ok(savedOrder);
    }

    // Actualizar una orden existente
    @PutMapping("/{id}")
    public ResponseEntity<PetOrder> updateOrder(@PathVariable Integer id, @RequestBody PetOrder orderDetails) {
        Optional<PetOrder> orderOptional = orderRepository.findById(id);

        if (!orderOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        PetOrder order = orderOptional.get();
        order.setProductName(orderDetails.getProductName());
        order.setQuantity(orderDetails.getQuantity());
        order.setOrderStatus(orderDetails.getOrderStatus());

        PetOrder updatedOrder = orderRepository.save(order);
        return ResponseEntity.ok(updatedOrder);
    }

    // Eliminar una orden
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        Optional<PetOrder> order = orderRepository.findById(id);

        if (!order.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        orderRepository.delete(order.get());
        return ResponseEntity.noContent().build();
    }

    // Buscar órdenes por estado
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PetOrder>> getOrdersByStatus(@PathVariable String status) {
        List<PetOrder> orders = orderRepository.findByOrderStatus(status);
        return ResponseEntity.ok(orders);
    }

    // Buscar órdenes por nombre de producto
    @GetMapping("/search")
    public ResponseEntity<List<PetOrder>> searchOrdersByProductName(@RequestParam String productName) {
        List<PetOrder> orders = orderRepository.findByProductNameContainingIgnoreCase(productName);
        return ResponseEntity.ok(orders);
    }
}
