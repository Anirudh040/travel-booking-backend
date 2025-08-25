package com.examly.springapp.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.examly.springapp.dto.ReportDTOs;
import com.examly.springapp.model.Reservation;
import com.examly.springapp.model.ReservationType;
import com.examly.springapp.repository.ReservationRepository;

@Service
public class ReportService {
    private final ReservationRepository repo;

    @Value("${app.booking.fee:50}")
    private BigDecimal bookingFee;

    public ReportService(ReservationRepository repo){ 
        this.repo = repo; 
    }

    public ReportDTOs.Summary summary(){
        List<Reservation> all = repo.findAll();
        ReportDTOs.Summary s = new ReportDTOs.Summary();
        s.totalBookings = all.size();
        s.flights = all.stream().filter(r -> r.getType()==ReservationType.FLIGHT).count();
        s.hotels = all.stream().filter(r -> r.getType()==ReservationType.HOTEL).count();
        s.revenue = bookingFee.multiply(BigDecimal.valueOf(all.size()));
        return s;
    }

    public List<ReportDTOs.TrendPoint> trend(LocalDate from, LocalDate to){
        if(from==null || to==null || to.isBefore(from)) { 
            from = LocalDate.now().minusDays(30); 
            to = LocalDate.now(); 
        }
        List<Reservation> list = repo.findByStartDateBetween(from, to);
        List<ReportDTOs.TrendPoint> points = new ArrayList<>();
        LocalDate d = from;
        while(!d.isAfter(to)){
            final LocalDate current = d; // ✅ effectively final for lambda
            long count = list.stream()
                    .filter(r -> r.getStartDate()!=null && r.getStartDate().equals(current))
                    .count();
            points.add(new ReportDTOs.TrendPoint(current, count));
            d = d.plusDays(1);
        }
        return points;
    }
}
