package com.pokemon.rankings.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import java.time.LocalDateTime;

@DynamoDBTable(tableName = "Groups")
public class Group {
    
    @DynamoDBHashKey(attributeName = "groupId")
    private String groupId;
    
    @DynamoDBAttribute(attributeName = "name")
    private String name;
    
    @DynamoDBAttribute(attributeName = "description")
    private String description;
    
    @DynamoDBAttribute(attributeName = "ownerId")
    private String ownerId;
    
    @DynamoDBAttribute(attributeName = "createdAt")
    private String createdAt;
    
    @DynamoDBAttribute(attributeName = "updatedAt")
    private String updatedAt;
    
    @DynamoDBAttribute(attributeName = "isActive")
    private Boolean isActive = true;
    
    // Constructors
    public Group() {}
    
    public Group(String name, String description, String ownerId) {
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.createdAt = LocalDateTime.now().toString();
        this.updatedAt = LocalDateTime.now().toString();
    }
    
    // Getters and Setters
    public String getGroupId() {
        return groupId;
    }
    
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getOwnerId() {
        return ownerId;
    }
    
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    // Helper methods
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now().toString();
    }
} 