package com.pokemon.rankings.controller;

import com.pokemon.rankings.dto.AddCardRequest;
import com.pokemon.rankings.dto.CardCollectionResponse;
import com.pokemon.rankings.service.CardCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/card-collection")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class CardCollectionController {
    
    @Autowired
    private CardCollectionService cardCollectionService;
    
    @PostMapping("/add")
    public ResponseEntity<?> addCardToCollection(
            @Valid @RequestBody AddCardRequest request,
            Authentication authentication) {
        try {
            System.out.println("[DEBUG] CardCollectionController.addCardToCollection called");
            System.out.println("[DEBUG] Authentication: " + authentication);
            System.out.println("[DEBUG] Request: " + request);
            
            String userId = authentication.getName();
            System.out.println("[DEBUG] User ID: " + userId);
            
            CardCollectionResponse response = cardCollectionService.addCardToCollection(userId, request);
            System.out.println("[DEBUG] Response: " + response);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.out.println("[DEBUG] RuntimeException in addCardToCollection: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println("[DEBUG] Exception in addCardToCollection: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Unexpected error: " + e.getMessage());
        }
    }
    
    @GetMapping("/my-collection")
    public ResponseEntity<List<CardCollectionResponse>> getMyCollection(Authentication authentication) {
        try {
            String userId = authentication.getName();
            List<CardCollectionResponse> collection = cardCollectionService.getUserCollection(userId);
            return ResponseEntity.ok(collection);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CardCollectionResponse>> getUserCollection(@PathVariable String userId) {
        try {
            List<CardCollectionResponse> collection = cardCollectionService.getUserCollection(userId);
            return ResponseEntity.ok(collection);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    @DeleteMapping("/remove/{cardId}")
    public ResponseEntity<Void> removeCardFromCollection(
            @PathVariable String cardId,
            Authentication authentication) {
        try {
            String userId = authentication.getName();
            cardCollectionService.removeCardFromCollection(userId, cardId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/update-quantity/{cardId}")
    public ResponseEntity<CardCollectionResponse> updateCardQuantity(
            @PathVariable String cardId,
            @RequestParam int quantity,
            Authentication authentication) {
        try {
            String userId = authentication.getName();
            CardCollectionResponse response = cardCollectionService.updateCardQuantity(userId, cardId, quantity);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 