package com.example.demo;


import com.example.demo.controllers.PetOrderController;
import com.example.demo.entity.PetOrder;
import com.example.demo.repository.PetOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PetOrderController.class)
public class PetOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetOrderRepository orderRepository;

    private PetOrder order1;
    private PetOrder order2;

    @BeforeEach
    void setUp() {
        order1 = new PetOrder(1, "Dog Food", 3, "Processing");
        order2 = new PetOrder(2, "Cat Food", 2, "Shipped");

        Mockito.when(orderRepository.findById(1)).thenReturn(Optional.of(order1));
        Mockito.when(orderRepository.findById(2)).thenReturn(Optional.of(order2));
        Mockito.when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));
    }

    @Test
    void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.petOrderList").exists())
                .andExpect(jsonPath("$._embedded.petOrderList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.petOrderList[1].id").value(2))
                .andExpect(jsonPath("$._embedded.petOrderList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.petOrderList[1]._links.self.href").exists());
    }

    @Test
    void testGetOrderById() throws Exception {
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productName").value("Dog Food"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.orders.href").exists());
    }

    @Test
    void testAddOrder() throws Exception {
        PetOrder newOrder = new PetOrder(null, "Fish Food", 5, "Pending");
        Mockito.when(orderRepository.save(Mockito.any(PetOrder.class))).thenReturn(newOrder);

        mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content("{ \"productName\": \"Fish Food\", \"quantity\": 5, \"orderStatus\": \"Pending\" }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.productName").value("Fish Food"))
                .andExpect(jsonPath("$.orderStatus").value("Pending"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.orders.href").exists());
    }

    @Test
    void testDeleteOrder() throws Exception {
        Mockito.when(orderRepository.existsById(1)).thenReturn(true);
        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isNoContent());
    }
}
