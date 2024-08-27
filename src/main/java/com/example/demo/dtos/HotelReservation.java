package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class HotelReservation {
    private int id;
    private int roomNumber;
    private String guestName;
    private String checkInDate;
    private String checkOutDate;
}
