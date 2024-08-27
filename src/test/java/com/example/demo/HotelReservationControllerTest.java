package com.example.demo;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.controllers.HotelReservationController;
import com.example.demo.dtos.HotelReservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

public class HotelReservationControllerTest {

    @Test
    public void testGetReservations() {
        HotelReservationController controller = new HotelReservationController();
        ResponseEntity<ArrayList<HotelReservation>> response = controller.getReservations();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetReservationById() {
        HotelReservationController controller = new HotelReservationController();
        ResponseEntity<HotelReservation> response = controller.getReservationById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testAddReservation() {
        HotelReservationController controller = new HotelReservationController();
        HotelReservation newReservation = new HotelReservation(9, 109, "Laura Campos", "2024-10-12", "2024-10-18");
        ResponseEntity<HotelReservation> response = controller.addReservation(newReservation);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(9, response.getBody().getId());
    }

    @Test
    public void testCheckAvailability() {
        HotelReservationController controller = new HotelReservationController();
        ResponseEntity<Boolean> response = controller.checkAvailability(101, "2024-09-02", "2024-09-04");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody());

        response = controller.checkAvailability(101, "2024-09-06", "2024-09-08");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }
}