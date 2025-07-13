package com.pokemon.rankings.dto;

import com.pokemon.rankings.entity.Group;
import com.pokemon.rankings.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class GroupResponse {
    
    private Long id;
    private String name;
    private String description;
    private UserSummary owner;
    private List<UserSummary> members;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    
    // Constructors
    public GroupResponse() {}
    
    public GroupResponse(Group group) {
        this.id = group.getId();
        this.name = group.getName();
        this.description = group.getDescription();
        this.owner = new UserSummary(group.getOwner());
        this.members = group.getMembers().stream()
                .map(UserSummary::new)
                .collect(Collectors.toList());
        this.createdAt = group.getCreatedAt();
        this.updatedAt = group.getUpdatedAt();
        this.isActive = group.getIsActive();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public UserSummary getOwner() {
        return owner;
    }
    
    public void setOwner(UserSummary owner) {
        this.owner = owner;
    }
    
    public List<UserSummary> getMembers() {
        return members;
    }
    
    public void setMembers(List<UserSummary> members) {
        this.members = members;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    // Inner class for user summary
    public static class UserSummary {
        private Long id;
        private String username;
        private String email;
        
        public UserSummary() {}
        
        public UserSummary(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
        }
        
        // Getters and Setters
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
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