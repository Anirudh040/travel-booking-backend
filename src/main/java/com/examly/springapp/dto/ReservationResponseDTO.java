package com.examly.springapp.dto;

import com.examly.springapp.model.ReservationStatus;
import com.examly.springapp.model.ReservationType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReservationResponseDTO {
    public Long id;
    public ReservationType type;
    public ReservationStatus status;
    public Integer seatsOrRooms;
    public BigDecimal totalPrice;
    public LocalDate startDate;
    public LocalDate endDate;
    public Long flightId;
    public Long hotelId;
    public String summary;
}
