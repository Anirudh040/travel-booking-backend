package com.examly.springapp.service;

import com.examly.springapp.dto.AuthDtos;
import com.examly.springapp.exception.ApiException;
import com.examly.springapp.model.Role;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder encoder, JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public User register(AuthDtos.RegisterRequest req){
        userRepository.findByEmail(req.email)
                .ifPresent(u -> { throw new ApiException("Email already in use"); });

        User u = new User();
        u.setName(req.name);
        u.setEmail(req.email);
        u.setPasswordHash(encoder.encode(req.password));

        // ✅ Role assignment from request
        Role role;
        try {
            role = Role.valueOf(req.role.toUpperCase());
        } catch (Exception e) {
            role = Role.CUSTOMER; // default fallback if invalid or missing
        }
        u.setRole(role);

        u.setActive(true);
        return userRepository.save(u);
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest req){
        User u = userRepository.findByEmail(req.email)
                .orElseThrow(() -> new ApiException("Invalid credentials"));

        if(!u.isActive()) throw new ApiException("Account is deactivated");
        if(!encoder.matches(req.password, u.getPasswordHash()))
            throw new ApiException("Invalid credentials");

        String token = jwtUtil.generateToken(u.getEmail(), u.getRole().name());
        return new AuthDtos.AuthResponse(token, u.getRole().name());
    }
}
