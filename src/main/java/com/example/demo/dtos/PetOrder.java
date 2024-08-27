package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PetOrder {
    private int id;
    private String productName;
    private int quantity;
    private String orderStatus;
}
