package com.example.demo;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controllers.HotelReservationController;
import com.example.demo.entity.HotelReservation;
import com.example.demo.repository.HotelReservationRepository;

@WebMvcTest(HotelReservationController.class)
public class HotelReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelReservationRepository reservationRepository;

    private HotelReservation reservation1;
    private HotelReservation reservation2;

    @BeforeEach
    void setUp() {

         Date date =  new GregorianCalendar(2024, Calendar.FEBRUARY, 1).getTime();
         Date dateTo = new GregorianCalendar(2024, Calendar.FEBRUARY, 10).getTime();

         
         reservation1 = new HotelReservation(101, "Manuel Bastias", date, dateTo,  1);
         reservation2 = new HotelReservation(102, "Ignacio Moreno", date, dateTo,  2);
        

        Mockito.when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation1));
        Mockito.when(reservationRepository.findById(2)).thenReturn(Optional.of(reservation2));
        Mockito.when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation1, reservation2));
    }

    @Test
    void testGetAllReservations() throws Exception {
        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.hotelReservationList").exists())
                .andExpect(jsonPath("$._embedded.hotelReservationList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.hotelReservationList[1].id").value(2))
                .andExpect(jsonPath("$._embedded.hotelReservationList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.hotelReservationList[1]._links.self.href").exists());
    }

    @Test
    void testGetReservationById() throws Exception {
        mockMvc.perform(get("/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.guestName").value("Manuel Bastias"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.reservations.href").exists());
    }

    @Test
    void testAddReservation() throws Exception {
        Date date =  new GregorianCalendar(2024, Calendar.FEBRUARY, 1).getTime();
        Date dateTo = new GregorianCalendar(2024, Calendar.FEBRUARY, 10).getTime();

        
        HotelReservation newReservation = new HotelReservation(102, "Ignacio Moreno", date, dateTo,  2);
        Mockito.when(reservationRepository.save(Mockito.any(HotelReservation.class))).thenReturn(newReservation);

        mockMvc.perform(post("/reservations")
                        .contentType("application/json")
                        .content("{ \"roomNumber\": \"102\", \"guestName\": \"Alice\", \"check_in_date\": \"2024-02-01\" }, \"check_out_date\": \"2024-02-10\" }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.roomNumber").value("102"))
                .andExpect(jsonPath("$.guestName").value("Ignacio Moreno"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.reservations.href").exists());
    }

    @Test
    void testDeleteReservation() throws Exception {
        Mockito.when(reservationRepository.existsById(1)).thenReturn(true);
        mockMvc.perform(delete("/reservations/1"))
                .andExpect(status().isNoContent());
    }
}