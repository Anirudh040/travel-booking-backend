package com.examly.springapp.controller;

import com.examly.springapp.exception.ApiException;
import com.examly.springapp.model.Role;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserRepository repo;
    public UserController(UserRepository repo){ this.repo = repo; }

    @GetMapping("/users")
    public ResponseEntity<List<User>> all(){ return ResponseEntity.ok(repo.findAll()); }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> one(@PathVariable Long id){ return ResponseEntity.ok(repo.findById(id).orElseThrow(() -> new ApiException("User not found"))); }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User u){
        User e = repo.findById(id).orElseThrow(() -> new ApiException("User not found"));
        e.setName(u.getName()); e.setEmail(u.getEmail());
        return ResponseEntity.ok(repo.save(e));
    }

    // Admin endpoints
    @PutMapping("/admin/users/{id}/role")
    public ResponseEntity<User> setRole(@PathVariable Long id, @RequestParam Role role){
        User e = repo.findById(id).orElseThrow(() -> new ApiException("User not found"));
        e.setRole(role); return ResponseEntity.ok(repo.save(e));
    }

    @PutMapping("/admin/users/{id}/deactivate")
    public ResponseEntity<User> deactivate(@PathVariable Long id){
        User e = repo.findById(id).orElseThrow(() -> new ApiException("User not found"));
        e.setActive(false); return ResponseEntity.ok(repo.save(e));
    }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){ repo.deleteById(id); return ResponseEntity.noContent().build(); }
}
