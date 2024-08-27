package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dtos.PetOrder;
import java.util.ArrayList;

@RestController
public class PetOrderController {
    ArrayList<PetOrder> orders = new ArrayList<>();

    public PetOrderController() {
        orders.add(new PetOrder(1, "Alimento para Perros", 3, "Enviado"));
        orders.add(new PetOrder(2, "Juguete para Gatos", 2, "Entregado"));
        orders.add(new PetOrder(3, "Jaula para Pájaros", 1, "Procesando"));
        orders.add(new PetOrder(4, "Pecera", 1, "Enviado"));
        orders.add(new PetOrder(5, "Rascador para Gatos", 2, "Enviado"));
        orders.add(new PetOrder(6, "Correa para Perros", 1, "Procesando"));
        orders.add(new PetOrder(7, "Alimento para Gatos", 5, "Entregado"));
        orders.add(new PetOrder(8, "Collar para Perros", 2, "Entregado"));
    }

    @GetMapping("/orders")
    public ResponseEntity<ArrayList<PetOrder>> getOrders() {
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<PetOrder> getOrderById(@PathVariable("id") int orderId) {
        for (PetOrder order : orders) {
            if (order.getId() == orderId) {
                return ResponseEntity.ok(order);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/orders")
    public ResponseEntity<PetOrder> addOrder(@RequestBody PetOrder order) {
        orders.add(order);
        return ResponseEntity.ok(order);
    }
}
