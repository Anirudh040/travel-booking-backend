package com.examly.springapp.service;

import com.examly.springapp.dto.SearchDtos;
import com.examly.springapp.exception.ApiException;
import com.examly.springapp.model.Flight;
import com.examly.springapp.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FlightService {
    private final FlightRepository repo;
    public FlightService(FlightRepository repo){ this.repo = repo; }

    public List<Flight> search(SearchDtos.FlightSearch q){
        List<Flight> all = repo.findAll();
        return all.stream().filter(f -> {
            boolean ok = true;
            if(q.origin != null && !q.origin.isBlank()) ok &= f.getOrigin().equalsIgnoreCase(q.origin);
            if(q.destination != null && !q.destination.isBlank()) ok &= f.getDestination().equalsIgnoreCase(q.destination);
            if(q.airline != null && !q.airline.isBlank()) ok &= f.getAirline().equalsIgnoreCase(q.airline);
            if(q.maxPrice != null) ok &= f.getPrice().intValue() <= q.maxPrice;
            if(q.departureDate != null) {
                LocalDate d = f.getDepartureTime().toLocalDate();
                ok &= d.equals(q.departureDate);
            }
            return ok;
        }).toList();
    }

    public Flight get(Long id){ return repo.findById(id).orElseThrow(() -> new ApiException("Flight not found")); }
    public Flight create(Flight f){ return repo.save(f); }
    public Flight update(Long id, Flight f){
        Flight e = get(id);
        e.setAirline(f.getAirline());
        e.setFlightNumber(f.getFlightNumber());
        e.setOrigin(f.getOrigin());
        e.setDestination(f.getDestination());
        e.setDepartureTime(f.getDepartureTime());
        e.setArrivalTime(f.getArrivalTime());
        e.setPrice(f.getPrice());
        e.setAvailableSeats(f.getAvailableSeats());
        return repo.save(e);
    }
    public void delete(Long id){ repo.deleteById(id); }
}
