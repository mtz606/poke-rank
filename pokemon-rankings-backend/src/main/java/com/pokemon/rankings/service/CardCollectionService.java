package com.pokemon.rankings.service;

import com.pokemon.rankings.dto.AddCardRequest;
import com.pokemon.rankings.dto.CardCollectionResponse;
import com.pokemon.rankings.entity.UserCardCollection;
import com.pokemon.rankings.repository.UserCardCollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardCollectionService {
    
    @Autowired
    private UserCardCollectionRepository userCardCollectionRepository;
    
    public CardCollectionResponse addCardToCollection(String userId, AddCardRequest request) {
        // Check if user already has this card
        List<UserCardCollection> existingCards = userCardCollectionRepository.findByUserId(userId);
        UserCardCollection existingCard = existingCards.stream()
                .filter(card -> card.getCardId().equals(request.getCardId()))
                .findFirst()
                .orElse(null);
        
        if (existingCard != null) {
            // Update quantity if card already exists
            existingCard.setQuantity(existingCard.getQuantity() + request.getQuantity());
            userCardCollectionRepository.save(existingCard);
            
            return createCardCollectionResponse(existingCard, request);
        } else {
            // Create new card entry
            UserCardCollection newCard = new UserCardCollection(
                userId, 
                request.getCardId(), 
                request.getQuantity()
            );
            userCardCollectionRepository.save(newCard);
            
            return createCardCollectionResponse(newCard, request);
        }
    }
    
    public List<CardCollectionResponse> getUserCollection(String userId) {
        List<UserCardCollection> userCards = userCardCollectionRepository.findByUserId(userId);
        
        return userCards.stream()
                .map(card -> {
                    CardCollectionResponse response = new CardCollectionResponse(
                        card.getCardId(),
                        "Card Name", // This would come from a card details service
                        card.getQuantity(),
                        card.getAcquiredAt()
                    );
                    
                    // Set estimated value (placeholder for now)
                    response.setEstimatedValue(1.0 * card.getQuantity()); // $1 per card placeholder
                    
                    return response;
                })
                .collect(Collectors.toList());
    }
    
    public void removeCardFromCollection(String userId, String cardId) {
        List<UserCardCollection> userCards = userCardCollectionRepository.findByUserId(userId);
        UserCardCollection cardToRemove = userCards.stream()
                .filter(card -> card.getCardId().equals(cardId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Card not found in collection"));
        
        userCardCollectionRepository.batchDelete(List.of(cardToRemove));
    }
    
    public CardCollectionResponse updateCardQuantity(String userId, String cardId, int quantity) {
        List<UserCardCollection> userCards = userCardCollectionRepository.findByUserId(userId);
        UserCardCollection cardToUpdate = userCards.stream()
                .filter(card -> card.getCardId().equals(cardId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Card not found in collection"));
        
        if (quantity <= 0) {
            // Remove card if quantity is 0 or negative
            userCardCollectionRepository.batchDelete(List.of(cardToUpdate));
            return null;
        } else {
            // Update quantity
            cardToUpdate.setQuantity(quantity);
            userCardCollectionRepository.save(cardToUpdate);
            
            return createCardCollectionResponse(cardToUpdate, null);
        }
    }
    
    private CardCollectionResponse createCardCollectionResponse(UserCardCollection card, AddCardRequest request) {
        CardCollectionResponse response = new CardCollectionResponse(
            card.getCardId(),
            request != null ? request.getCardName() : "Card Name",
            card.getQuantity(),
            card.getAcquiredAt()
        );
        
        if (request != null) {
            response.setCardImage(request.getCardImage());
            response.setCardSet(request.getCardSet());
            response.setCardRarity(request.getCardRarity());
        }
        
        // Set estimated value (placeholder for now)
        response.setEstimatedValue(1.0 * card.getQuantity()); // $1 per card placeholder
        
        return response;
    }
} 