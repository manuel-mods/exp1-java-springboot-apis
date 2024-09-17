package com.example.demo.entity;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "hotel_reservations")
public class HotelReservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "room_number")
    private Integer roomNumber;

    @Column(name = "guest_name")
    private String guestName;

    @Column(name = "check_in_date")
    private Date checkInDate;

    @Column(name = "check_out_date")
    private Date checkOutDate;

    public HotelReservation() {
    }

    public HotelReservation(Integer roomNumber, String guestName, Date checkInDate, Date checkOutDate, Integer id) {
        this.roomNumber = roomNumber;
        this.guestName = guestName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.id = id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

}
