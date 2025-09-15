package com.pokemon.rankings.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import java.time.LocalDateTime;

@DynamoDBTable(tableName = "UserCardCollection")
public class UserCardCollection {
    @DynamoDBHashKey(attributeName = "userId")
    private String userId;

    @DynamoDBRangeKey(attributeName = "cardId")
    private String cardId;

    @DynamoDBAttribute(attributeName = "quantity")
    private int quantity = 1;

    @DynamoDBAttribute(attributeName = "acquiredAt")
    private String acquiredAt;

    public UserCardCollection() {}

    public UserCardCollection(String userId, String cardId, int quantity) {
        this.userId = userId;
        this.cardId = cardId;
        this.quantity = quantity;
        this.acquiredAt = LocalDateTime.now().toString();
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCardId() { return cardId; }
    public void setCardId(String cardId) { this.cardId = cardId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getAcquiredAt() { return acquiredAt; }
    public void setAcquiredAt(String acquiredAt) { this.acquiredAt = acquiredAt; }
} 