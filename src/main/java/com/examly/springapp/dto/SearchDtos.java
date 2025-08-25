package com.examly.springapp.dto;

import java.time.LocalDate;

public class SearchDtos {
    public static class FlightSearch {
        public String origin;
        public String destination;
        public LocalDate departureDate;
        public String airline;
        public Integer maxPrice;
    }
    public static class HotelSearch {
        public String location;
        public LocalDate checkInDate;
        public LocalDate checkOutDate;
        public Integer maxPrice;
        public String hotelName;
    }
}
