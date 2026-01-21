package com.pokemon.rankings.service;

import com.pokemon.rankings.dto.PokemonCardResponse;
import com.pokemon.rankings.dto.PokemonCardsPageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Service
public class PokemonTcgService {
    
    @Value("${pokemon.tcg.api.key:}")
    private String apiKey;
    
    @Value("${pokemon.tcg.api.base-url:https://api.pokemontcg.io/v2}")
    private String baseUrl;
    
    private final RestTemplate restTemplate;
    
    public PokemonTcgService() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000); // 10 seconds
        factory.setReadTimeout(45000); // 45 seconds (API can be slow with large result sets)
        this.restTemplate = new RestTemplate(factory);
    }
    
    public List<PokemonCardResponse> searchCards(String query, int page, int pageSize) {
        // Handle empty query - return all cards (no query parameter)
        String formattedQuery = null;
        if (query != null && !query.trim().isEmpty()) {
            // Format query for Pokemon TCG API v2
            // If query doesn't contain a colon (field:value), assume it's a name search
            formattedQuery = query.trim();
            if (!formattedQuery.contains(":")) {
                // If query contains spaces, wrap in quotes for phrase search
                // This makes the search more specific and faster
                if (formattedQuery.contains(" ")) {
                    formattedQuery = "name:\"" + formattedQuery + "\"";
                } else {
                    formattedQuery = "name:" + formattedQuery;
                }
            }
        }
        
        // For loading all cards, use larger page size (up to 250)
        // For searches, use smaller page size for faster responses
        int effectivePageSize;
        if (formattedQuery == null) {
            // Loading all cards - use requested pageSize up to 250
            effectivePageSize = Math.min(Math.max(pageSize, 1), 250);
        } else {
            // Searching - use smaller page size for faster responses
            effectivePageSize = Math.min(Math.max(pageSize, 1), 20);
        }
        
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/cards")
                .queryParam("page", page)
                .queryParam("pageSize", effectivePageSize);
        
        // Only add query parameter if we have a search query
        if (formattedQuery != null) {
            builder.queryParam("q", formattedQuery);
        }
        
        HttpHeaders headers = new HttpHeaders();
        if (apiKey != null && !apiKey.isEmpty()) {
            headers.set("X-Api-Key", apiKey);
        }
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            System.out.println("[PokemonTcgService] Searching: " + formattedQuery);
            ResponseEntity<PokemonCardsPageResponse> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    PokemonCardsPageResponse.class
            );
            
            if (response.getBody() != null && response.getBody().getData() != null) {
                System.out.println("[PokemonTcgService] Found " + response.getBody().getData().size() + " cards");
                return response.getBody().getData();
            }
            return Collections.emptyList();
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            System.err.println("[PokemonTcgService] HTTP Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new RuntimeException("Pokemon TCG API error: " + e.getStatusCode() + " - " + 
                (e.getResponseBodyAsString() != null ? e.getResponseBodyAsString() : e.getMessage()), e);
        } catch (org.springframework.web.client.ResourceAccessException e) {
            System.err.println("[PokemonTcgService] Connection/Timeout Error: " + e.getMessage());
            throw new RuntimeException("Pokemon TCG API is currently unavailable. Please try again later.", e);
        } catch (Exception e) {
            System.err.println("[PokemonTcgService] Unexpected error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to search cards: " + e.getMessage(), e);
        }
    }
    
    public PokemonCardResponse getCardById(String cardId) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/cards/" + cardId);
        
        HttpHeaders headers = new HttpHeaders();
        if (apiKey != null && !apiKey.isEmpty()) {
            headers.set("X-Api-Key", apiKey);
        }
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<PokemonCardResponse> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    PokemonCardResponse.class
            );
            
            return response.getBody();
        } catch (Exception e) {
            System.err.println("[PokemonTcgService] Error fetching card: " + e.getMessage());
            throw new RuntimeException("Failed to fetch card: " + e.getMessage(), e);
        }
    }
}

