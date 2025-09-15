package com.pokemon.rankings.dto;

import java.time.LocalDateTime;

public class CardCollectionResponse {
    private String cardId;
    private String cardName;
    private int quantity;
    private String acquiredAt;
    private String cardImage;
    private String cardSet;
    private String cardRarity;
    private Double estimatedValue;
    
    public CardCollectionResponse() {}
    
    public CardCollectionResponse(String cardId, String cardName, int quantity, String acquiredAt) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.quantity = quantity;
        this.acquiredAt = acquiredAt;
    }
    
    public String getCardId() {
        return cardId;
    }
    
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    
    public String getCardName() {
        return cardName;
    }
    
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public String getAcquiredAt() {
        return acquiredAt;
    }
    
    public void setAcquiredAt(String acquiredAt) {
        this.acquiredAt = acquiredAt;
    }
    
    public String getCardImage() {
        return cardImage;
    }
    
    public void setCardImage(String cardImage) {
        this.cardImage = cardImage;
    }
    
    public String getCardSet() {
        return cardSet;
    }
    
    public void setCardSet(String cardSet) {
        this.cardSet = cardSet;
    }
    
    public String getCardRarity() {
        return cardRarity;
    }
    
    public void setCardRarity(String cardRarity) {
        this.cardRarity = cardRarity;
    }
    
    public Double getEstimatedValue() {
        return estimatedValue;
    }
    
    public void setEstimatedValue(Double estimatedValue) {
        this.estimatedValue = estimatedValue;
    }
} 