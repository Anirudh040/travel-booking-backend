package com.examly.springapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {

    public static class RegisterRequest {
        @NotBlank public String name;
        @Email public String email;
        @NotBlank public String password;
        @NotBlank public String role;   // ✅ Added role field
    }

    public static class LoginRequest {
        @Email public String email;
        @NotBlank public String password;
    }

    public static class AuthResponse {
        public String token;
        public String role;
        public AuthResponse(String token, String role) {
            this.token = token;
            this.role = role;
        }
    }
}
