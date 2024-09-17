package com.example.demo.repository;

import com.example.demo.entity.PetOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetOrderRepository extends JpaRepository<PetOrder, Integer> {

    List<PetOrder> findByOrderStatus(String orderStatus);

    List<PetOrder> findByProductNameContainingIgnoreCase(String productName);
}
