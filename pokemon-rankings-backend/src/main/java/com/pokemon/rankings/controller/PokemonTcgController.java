package com.pokemon.rankings.controller;

import com.pokemon.rankings.dto.PokemonCardResponse;
import com.pokemon.rankings.service.PokemonTcgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/pokemon-tcg")
public class PokemonTcgController {
    
    @Autowired
    private PokemonTcgService pokemonTcgService;
    
    @GetMapping("/search")
    public ResponseEntity<?> searchCards(
            @RequestParam(required = false, defaultValue = "") String q,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        try {
            System.out.println("[PokemonTcgController] Search request: q=" + q + ", page=" + page + ", pageSize=" + pageSize);
            List<PokemonCardResponse> cards = pokemonTcgService.searchCards(q, page, pageSize);
            System.out.println("[PokemonTcgController] Returning " + cards.size() + " cards");
            return ResponseEntity.ok(cards);
        } catch (RuntimeException e) {
            System.err.println("[PokemonTcgController] Error: " + e.getMessage());
            return ResponseEntity.status(500)
                .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            System.err.println("[PokemonTcgController] Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                .body(Collections.singletonMap("error", "An unexpected error occurred: " + e.getMessage()));
        }
    }
    
    @GetMapping("/cards/{cardId}")
    public ResponseEntity<?> getCardById(@PathVariable String cardId) {
        try {
            PokemonCardResponse card = pokemonTcgService.getCardById(cardId);
            if (card != null) {
                return ResponseEntity.ok(card);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(404)
                .body(Collections.singletonMap("error", "Card not found: " + e.getMessage()));
        }
    }
}

