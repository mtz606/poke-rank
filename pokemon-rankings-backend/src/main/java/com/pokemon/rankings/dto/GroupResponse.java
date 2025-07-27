package com.pokemon.rankings.dto;

import com.pokemon.rankings.entity.Group;
import com.pokemon.rankings.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupResponse {
    
    private String groupId;
    private String name;
    private String description;
    private String ownerId;
    private List<UserSummary> members;
    private String createdAt;
    private String updatedAt;
    private Boolean isActive;
    
    // Constructors
    public GroupResponse() {}
    
    public GroupResponse(Group group) {
        this.groupId = group.getGroupId();
        this.name = group.getName();
        this.description = group.getDescription();
        this.ownerId = group.getOwnerId();
        this.members = List.of(); // Will be populated separately if needed
        this.createdAt = group.getCreatedAt();
        this.updatedAt = group.getUpdatedAt();
        this.isActive = group.getIsActive();
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
    
    public List<UserSummary> getMembers() {
        return members;
    }
    
    public void setMembers(List<UserSummary> members) {
        this.members = members;
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
    
    // Inner class for user summary
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserSummary {
        private String userId;
        private String username;
        private String email;
        
        public UserSummary() {}
        
        public UserSummary(User user) {
            this.userId = user.getUserId();
            this.username = user.getUsername();
            this.email = user.getEmail();
        }
        
        // Getters and Setters
        public String getUserId() {
            return userId;
        }
        
        public void setUserId(String userId) {
            this.userId = userId;
        }
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
    }
} 