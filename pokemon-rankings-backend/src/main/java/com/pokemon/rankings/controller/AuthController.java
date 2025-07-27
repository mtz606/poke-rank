package com.pokemon.rankings.controller;

import com.pokemon.rankings.dto.LoginRequest;
import com.pokemon.rankings.dto.RegisterRequest;
import com.pokemon.rankings.dto.AuthResponse;
import com.pokemon.rankings.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class AuthController {
    
    private final AuthenticationService authenticationService;
    
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        System.out.println("[DEBUG] /auth/register called with: username=" + request.getUsername() + ", email=" + request.getEmail());
        try {
            AuthResponse response = authenticationService.register(request);
            System.out.println("[DEBUG] Registration successful for username=" + request.getUsername());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.out.println("[DEBUG] Registration failed for username=" + request.getUsername() + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println("[DEBUG] Unexpected error during registration: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Unexpected error: " + e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authenticationService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth endpoint is working!");
    }
    
    @PostMapping("/debug")
    public ResponseEntity<String> debug(@RequestBody LoginRequest request) {
        return ResponseEntity.ok("Received: " + request.getUsername() + ", " + request.getPassword());
    }
} 