package com.pokemon.rankings.service;

import com.pokemon.rankings.dto.LoginRequest;
import com.pokemon.rankings.dto.RegisterRequest;
import com.pokemon.rankings.dto.AuthResponse;
import com.pokemon.rankings.entity.User;
import com.pokemon.rankings.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, 
                               JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    
    public AuthResponse register(RegisterRequest request) {
        System.out.println("[DEBUG] AuthenticationService.register called with: username=" + request.getUsername() + ", email=" + request.getEmail());
        // Check if user already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            System.out.println("[DEBUG] Username already exists: " + request.getUsername());
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            System.out.println("[DEBUG] Email already exists: " + request.getEmail());
            throw new RuntimeException("Email already exists");
        }
        
        // Create new user with UUID as userId
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        
        System.out.println("[DEBUG] Saving new user to DynamoDB: username=" + user.getUsername() + ", email=" + user.getEmail());
        User savedUser = userRepository.save(user);
        System.out.println("[DEBUG] User saved successfully: userId=" + savedUser.getUserId());
        String jwtToken = jwtService.generateToken(savedUser);
        
        return new AuthResponse(jwtToken, savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole());
    }
    
    public AuthResponse authenticate(LoginRequest request) {
        try {
            System.out.println("[DEBUG] AuthenticationService.authenticate called with: username=" + request.getUsername());
            
            // First check if user exists
            System.out.println("[DEBUG] Looking for user: " + request.getUsername());
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> {
                        System.out.println("[DEBUG] User not found: " + request.getUsername());
                        return new RuntimeException("User not found");
                    });
            
            System.out.println("[DEBUG] User found: " + user.getUsername() + ", email: " + user.getEmail() + ", password hash: " + user.getPassword().substring(0, Math.min(20, user.getPassword().length())) + "...");
            
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            
            System.out.println("[DEBUG] Authentication successful for: " + request.getUsername());
            
            String jwtToken = jwtService.generateToken(user);
            System.out.println("[DEBUG] JWT token generated successfully");
            
            return new AuthResponse(jwtToken, user.getUsername(), user.getEmail(), user.getRole());
        } catch (Exception e) {
            System.out.println("[DEBUG] Authentication failed for: " + request.getUsername() + ", error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Invalid username or password");
        }
    }
} 