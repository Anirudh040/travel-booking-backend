package com.examly.springapp.controller;

import com.examly.springapp.dto.BookingRequestDTO;
import com.examly.springapp.dto.ReservationResponseDTO;
import com.examly.springapp.model.Reservation;
import com.examly.springapp.model.ReservationStatus;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class ReservationController {
    private final ReservationService service;
    private final UserRepository userRepo;
    public ReservationController(ReservationService service, UserRepository userRepo){
        this.service = service; this.userRepo = userRepo;
    }

    @PostMapping("/api/reservations")
    public ResponseEntity<ReservationResponseDTO> book(Principal principal, @Valid @RequestBody BookingRequestDTO req){
        return ResponseEntity.ok(service.book(principal.getName(), req));
    }

    @GetMapping("/api/reservations/customer")
    public ResponseEntity<List<Reservation>> myReservations(Principal principal){
        User user = userRepo.findByEmail(principal.getName()).orElseThrow();
        return ResponseEntity.ok(service.forUser(user));
    }

    @PutMapping("/api/reservations/{id}")
    public ResponseEntity<Reservation> cancel(Principal principal, @PathVariable Long id, @RequestParam(defaultValue = "CANCELED") String status){
        if(!"CANCELED".equalsIgnoreCase(status)) status = "CANCELED";
        return ResponseEntity.ok(service.cancel(id, principal.getName()));
    }

    // Admin status update
    @PutMapping("/api/admin/reservations/{id}/status")
    public ResponseEntity<Reservation> adminUpdate(@PathVariable Long id, @RequestParam ReservationStatus status){
        return ResponseEntity.ok(service.adminUpdateStatus(id, status));
    }
}
