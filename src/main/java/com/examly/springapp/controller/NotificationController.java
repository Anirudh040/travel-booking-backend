package com.examly.springapp.controller;

import com.examly.springapp.model.Notification;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService service;
    private final UserRepository userRepo;
    public NotificationController(NotificationService service, UserRepository userRepo){
        this.service = service; this.userRepo = userRepo;
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> unread(Principal principal){
        User u = userRepo.findByEmail(principal.getName()).orElseThrow();
        return ResponseEntity.ok(service.unread(u));
    }
}
