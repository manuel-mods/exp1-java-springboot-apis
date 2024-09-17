package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "pet_orders")
public class PetOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "order_status")
    private String orderStatus;

    public PetOrder() {
    }

    public PetOrder(String productName, Integer quantity, String orderStatus) {
        this.productName = productName;
        this.quantity = quantity;
        this.orderStatus = orderStatus;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
