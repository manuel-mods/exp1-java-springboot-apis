package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
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
import com.example.demo.entity.Room;
import com.example.demo.repository.HotelReservationRepository;
import com.example.demo.repository.RoomRepository;

@WebMvcTest(HotelReservationController.class)
public class HotelReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelReservationRepository reservationRepository;

    @MockBean
    private RoomRepository roomRepository;

    private HotelReservation reservation1;
    private HotelReservation reservation2;
    private Room room1;
    private Room room2;
    private Room room3;

    @BeforeEach
    void setUp() {
        // Fechas para las reservas
        
        java.util.Date checkInUtilDate = new GregorianCalendar(2024, Calendar.FEBRUARY, 1).getTime();
        java.util.Date checkOutUtilDate = new GregorianCalendar(2024, Calendar.FEBRUARY, 10).getTime();


        Date checkInDate = new Date(checkInUtilDate.getTime());
        Date checkOutDate = new Date(checkOutUtilDate.getTime());

        // Inicializar habitaciones
        room1 = new Room();
        room1.setId(1);
        room1.setRoomNumber(101);
        room1.setRoomType("Suite");

        room2 = new Room();
        room2.setId(2);
        room2.setRoomNumber(102);
        room2.setRoomType("Individual");

        room3 = new Room();
        room3.setId(3);
        room3.setRoomNumber(103);
        room3.setRoomType("Individual");

        // Inicializar reservas
        reservation1 = new HotelReservation(1, "Manuel Bastias", checkInDate, checkOutDate, 101);
        reservation2 = new HotelReservation(2, "Ignacio Moreno", checkInDate, checkOutDate, 102);

        // Mock para el repositorio de reservas
        Mockito.when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation1));
        Mockito.when(reservationRepository.findById(2)).thenReturn(Optional.of(reservation2));
        Mockito.when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation1, reservation2));

        // Mock para el repositorio de habitaciones
        Mockito.when(roomRepository.findAll()).thenReturn(Arrays.asList(room1, room2));
        Mockito.when(roomRepository.findByRoomNumber(101)).thenReturn(room1);
        Mockito.when(roomRepository.findByRoomNumber(102)).thenReturn(room2);
        Mockito.when(roomRepository.findByRoomNumber(103)).thenReturn(room3);

         // Mock para el repositorio de habitaciones
         Mockito.when(roomRepository.findAll()).thenReturn(Arrays.asList(room1, room2, room3));

    }

    @Test
    void testGetAllReservations() throws Exception {
        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.hotelReservationList").exists())
                .andExpect(jsonPath("$._embedded.hotelReservationList[0].id").value(101))
                .andExpect(jsonPath("$._embedded.hotelReservationList[1].id").value(102))
                .andExpect(jsonPath("$._embedded.hotelReservationList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.hotelReservationList[1]._links.self.href").exists());
    }

    @Test
    void testGetReservationById() throws Exception {
        mockMvc.perform(get("/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.guestName").value("Manuel Bastias"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.reservations.href").exists());
    }

    @Test
    void testAddReservation() throws Exception {
        java.util.Date checkInUtilDate = new GregorianCalendar(2024, Calendar.FEBRUARY, 1).getTime();
        java.util.Date checkOutUtilDate = new GregorianCalendar(2024, Calendar.FEBRUARY, 10).getTime();


        Date checkInDate = new Date(checkInUtilDate.getTime());
        Date checkOutDate = new Date(checkOutUtilDate.getTime());

        HotelReservation newReservation = new HotelReservation(103, "Alice Smith", checkInDate, checkOutDate, 3);
        Mockito.when(reservationRepository.save(Mockito.any(HotelReservation.class))).thenReturn(newReservation);

        mockMvc.perform(post("/reservations")
                        .contentType("application/json")
                        .content("{ \"roomNumber\": \"103\", \"guestName\": \"Alice Smith\", \"checkInDate\": \"2024-02-01\", \"checkOutDate\": \"2024-02-10\" }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.roomNumber").value("103"))
                .andExpect(jsonPath("$.guestName").value("Alice Smith"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.reservations.href").exists());
    }

    @Test
    void testDeleteReservation() throws Exception {
        Mockito.when(reservationRepository.existsById(1)).thenReturn(true);
        mockMvc.perform(delete("/reservations/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAvailableRooms() throws Exception {
        mockMvc.perform(get("/reservations/availability")
                        .param("checkInDate", "2024-02-15")
                        .param("checkOutDate", "2024-02-20")
                        .param("roomNumber", "101"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testGetRoomsAvailability() throws Exception {
        mockMvc.perform(get("/reservations/rooms-availability")
                        .param("checkInDate", "2024-02-01")
                        .param("checkOutDate", "2024-02-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableRooms").isArray())
                .andExpect(jsonPath("$.availableRooms[0].roomNumber").value("101"))
                .andExpect(jsonPath("$.availableRooms[1].roomNumber").value("102"))
                .andExpect(jsonPath("$.unavailableRooms").isArray());
    }
}
