package com.examly.springapp.controller;

import com.examly.springapp.dto.SearchDtos;
import com.examly.springapp.model.Hotel;
import com.examly.springapp.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class HotelController {
    private final HotelService service;
    public HotelController(HotelService service){ this.service = service; }

    @GetMapping("/api/hotels/search")
    public ResponseEntity<List<Hotel>> search(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String checkInDate,
            @RequestParam(required = false) String checkOutDate,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String hotelName
    ){
        SearchDtos.HotelSearch q = new SearchDtos.HotelSearch();
        q.location = location; q.maxPrice = maxPrice; q.hotelName = hotelName;
        if(checkInDate != null && !checkInDate.isBlank()) q.checkInDate = java.time.LocalDate.parse(checkInDate);
        if(checkOutDate != null && !checkOutDate.isBlank()) q.checkOutDate = java.time.LocalDate.parse(checkOutDate);
        return ResponseEntity.ok(service.search(q));
    }

    @GetMapping("/api/hotels/{id}")
    public ResponseEntity<Hotel> get(@PathVariable Long id){ return ResponseEntity.ok(service.get(id)); }

    // Admin CRUD
    @PostMapping("/api/admin/hotels")
    public ResponseEntity<Hotel> create(@RequestBody Hotel h){ return ResponseEntity.ok(service.create(h)); }

    @PutMapping("/api/admin/hotels/{id}")
    public ResponseEntity<Hotel> update(@PathVariable Long id, @RequestBody Hotel h){ return ResponseEntity.ok(service.update(id, h)); }

    @DeleteMapping("/api/admin/hotels/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){ service.delete(id); return ResponseEntity.noContent().build(); }
}
