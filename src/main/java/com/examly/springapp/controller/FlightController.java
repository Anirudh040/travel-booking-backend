package com.examly.springapp.controller;

import com.examly.springapp.dto.SearchDtos;
import com.examly.springapp.model.Flight;
import com.examly.springapp.service.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class FlightController {
    private final FlightService service;
    public FlightController(FlightService service){ this.service = service; }

    @GetMapping("/api/flights/search")
    public ResponseEntity<List<Flight>> search(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String departureDate,
            @RequestParam(required = false) String airline,
            @RequestParam(required = false) Integer maxPrice
    ){
        SearchDtos.FlightSearch q = new SearchDtos.FlightSearch();
        q.origin = origin; q.destination = destination; q.airline = airline; q.maxPrice = maxPrice;
        if(departureDate != null && !departureDate.isBlank()){
            q.departureDate = java.time.LocalDate.parse(departureDate);
        }
        return ResponseEntity.ok(service.search(q));
    }

    @GetMapping("/api/flights/{id}")
    public ResponseEntity<Flight> get(@PathVariable Long id){ return ResponseEntity.ok(service.get(id)); }

    // Admin CRUD
    @PostMapping("/api/admin/flights")
    public ResponseEntity<Flight> create(@RequestBody Flight f){ return ResponseEntity.ok(service.create(f)); }

    @PutMapping("/api/admin/flights/{id}")
    public ResponseEntity<Flight> update(@PathVariable Long id, @RequestBody Flight f){ return ResponseEntity.ok(service.update(id, f)); }

    @DeleteMapping("/api/admin/flights/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){ service.delete(id); return ResponseEntity.noContent().build(); }
}
