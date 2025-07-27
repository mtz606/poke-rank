package com.pokemon.rankings.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@DynamoDBTable(tableName = "Users")
public class User implements UserDetails {
    
    @DynamoDBHashKey(attributeName = "userId")
    private String userId;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @DynamoDBAttribute(attributeName = "username")
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @DynamoDBAttribute(attributeName = "email")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @DynamoDBAttribute(attributeName = "password")
    private String password;
    
    @DynamoDBAttribute(attributeName = "createdAt")
    private String createdAt;
    
    @DynamoDBAttribute(attributeName = "updatedAt")
    private String updatedAt;
    
    @DynamoDBAttribute(attributeName = "isActive")
    private String isActive = "true";
    
    @DynamoDBAttribute(attributeName = "role")
    private String role = "USER";
    
    // Default constructor
    public User() {
        this.userId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now().toString();
        this.updatedAt = LocalDateTime.now().toString();
        this.isActive = Boolean.TRUE.toString();
    }
    
    // Constructor with required fields
    public User(String username, String email, String password) {
        this.userId = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now().toString();
        this.updatedAt = LocalDateTime.now().toString();
        this.isActive = Boolean.TRUE.toString();
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
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
    
    public Boolean isActive() {
        return Boolean.parseBoolean(isActive);
    }
    
    public void setActive(Boolean active) {
        this.isActive = active.toString();
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    // UserDetails implementation
    @Override
    @DynamoDBIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }
    
    @Override
    @DynamoDBIgnore
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    @DynamoDBIgnore
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    @DynamoDBIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    @DynamoDBIgnore
    public boolean isEnabled() {
        return Boolean.parseBoolean(isActive);
    }
    
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now().toString();
    }
} 