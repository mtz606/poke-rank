package com.pokemon.rankings.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import java.time.LocalDateTime;

@DynamoDBTable(tableName = "GroupMembers")
public class GroupMember {
    
    @DynamoDBHashKey(attributeName = "groupId")
    private String groupId;
    
    @DynamoDBRangeKey(attributeName = "userId")
    private String userId;
    
    @DynamoDBAttribute(attributeName = "joinedAt")
    private String joinedAt;
    
    @DynamoDBAttribute(attributeName = "role")
    private String role = "MEMBER"; // "OWNER", "MEMBER", "ADMIN"
    
    // Constructors
    public GroupMember() {}
    
    public GroupMember(String groupId, String userId) {
        this.groupId = groupId;
        this.userId = userId;
        this.joinedAt = LocalDateTime.now().toString();
    }
    
    public GroupMember(String groupId, String userId, String role) {
        this(groupId, userId);
        this.role = role;
    }
    
    // Getters and Setters
    public String getGroupId() {
        return groupId;
    }
    
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getJoinedAt() {
        return joinedAt;
    }
    
    public void setJoinedAt(String joinedAt) {
        this.joinedAt = joinedAt;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
} 