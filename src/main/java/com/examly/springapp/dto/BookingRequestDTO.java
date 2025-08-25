package com.examly.springapp.dto;

import com.examly.springapp.model.ReservationType;
import java.time.LocalDate;

public class BookingRequestDTO {
    private ReservationType bookingType;
    private Long referenceId; // flightId or hotelId
    private Integer seatsOrRooms;
    private LocalDate startDate;
    private LocalDate endDate;

    public ReservationType getBookingType() { return bookingType; }
    public void setBookingType(ReservationType bookingType) { this.bookingType = bookingType; }
    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
    public Integer getSeatsOrRooms() { return seatsOrRooms; }
    public void setSeatsOrRooms(Integer seatsOrRooms) { this.seatsOrRooms = seatsOrRooms; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
