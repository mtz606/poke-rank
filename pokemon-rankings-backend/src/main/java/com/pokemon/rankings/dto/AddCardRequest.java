package com.pokemon.rankings.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AddCardRequest {
    @NotBlank(message = "Card ID is required")
    private String cardId;
    
    @NotBlank(message = "Card name is required")
    private String cardName;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    private String cardImage;
    private String cardSet;
    private String cardRarity;
    
    public AddCardRequest() {}
    
    public AddCardRequest(String cardId, String cardName, Integer quantity) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.quantity = quantity;
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
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
} 