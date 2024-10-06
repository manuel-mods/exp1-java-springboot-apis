package com.example.demo.repository;

import com.example.demo.entity.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends CrudRepository<Room, Integer> {
    Room findByRoomNumber(Integer roomNumber);
}
