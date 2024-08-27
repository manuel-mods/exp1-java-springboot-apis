package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.controllers.PetOrderController;
import com.example.demo.dtos.PetOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

public class PetOrderControllerTest {

    @Test
    public void testGetOrders() {
        PetOrderController controller = new PetOrderController();
        ResponseEntity<ArrayList<PetOrder>> response = controller.getOrders();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetOrderById() {
        PetOrderController controller = new PetOrderController();
        ResponseEntity<PetOrder> response = controller.getOrderById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testAddOrder() {
        PetOrderController controller = new PetOrderController();
        PetOrder newOrder = new PetOrder(3, "Fish Tank", 1, "Processing");
        ResponseEntity<PetOrder> response = controller.addOrder(newOrder);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().getId());
    }
}
