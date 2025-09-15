package com.pokemon.rankings.security;

import com.pokemon.rankings.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        System.out.println("[DEBUG] JWT Filter - Request path: " + path);
        
        boolean shouldSkip = path.contains("/auth/register") || 
                           path.contains("/auth/login") || 
                           path.contains("/auth/test") ||
                           path.contains("/auth/debug");
        
        System.out.println("[DEBUG] JWT Filter - Should skip: " + shouldSkip);
        return shouldSkip;
    }
    
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        System.out.println("[DEBUG] JWT Filter - Processing request: " + request.getRequestURI());
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("[DEBUG] JWT Filter - No Bearer token, continuing...");
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            jwt = authHeader.substring(7);
            System.out.println("[DEBUG] JWT Filter - Extracted JWT: " + jwt.substring(0, Math.min(50, jwt.length())) + "...");
            username = jwtService.extractUsername(jwt);
            System.out.println("[DEBUG] JWT Filter - Extracted username: " + username);
            
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                System.out.println("[DEBUG] JWT Filter - Loading user details for: " + username);
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                System.out.println("[DEBUG] JWT Filter - User details loaded: " + userDetails.getUsername());
                
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    System.out.println("[DEBUG] JWT Filter - Token is valid, setting authentication");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("[DEBUG] JWT Filter - Authentication set successfully");
                } else {
                    System.out.println("[DEBUG] JWT Filter - Token is invalid");
                }
            } else {
                System.out.println("[DEBUG] JWT Filter - Username is null or authentication already exists");
            }
        } catch (Exception e) {
            System.out.println("[DEBUG] JWT Filter - Error processing token: " + e.getMessage());
            e.printStackTrace();
        }
        
        filterChain.doFilter(request, response);
    }
} 