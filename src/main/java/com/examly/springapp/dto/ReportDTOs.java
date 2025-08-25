package com.examly.springapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;


public class ReportDTOs {
    public static class Summary {
        public long totalBookings;
        public long flights;
        public long hotels;
        public BigDecimal revenue;
    }
    public static class TrendPoint {
        public LocalDate date;
        public long bookings;
        public TrendPoint(LocalDate d, long b){ date=d; bookings=b; }
    }
}
