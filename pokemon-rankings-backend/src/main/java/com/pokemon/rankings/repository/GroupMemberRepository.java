package com.pokemon.rankings.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.pokemon.rankings.entity.GroupMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GroupMemberRepository {
    
    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    
    public GroupMember save(GroupMember groupMember) {
        dynamoDBMapper.save(groupMember);
        return groupMember;
    }
    
    public List<GroupMember> findByGroupId(String groupId) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":groupId", new AttributeValue().withS(groupId));
        
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("groupId = :groupId")
                .withExpressionAttributeValues(eav);
        
        return dynamoDBMapper.scan(GroupMember.class, scanExpression);
    }
    
    public List<GroupMember> findByUserId(String userId) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":userId", new AttributeValue().withS(userId));
        
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("userId = :userId")
                .withExpressionAttributeValues(eav);
        
        return dynamoDBMapper.scan(GroupMember.class, scanExpression);
    }
    
    public Optional<GroupMember> findByGroupIdAndUserId(String groupId, String userId) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":groupId", new AttributeValue().withS(groupId));
        eav.put(":userId", new AttributeValue().withS(userId));
        
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("groupId = :groupId AND userId = :userId")
                .withExpressionAttributeValues(eav);
        
        List<GroupMember> members = dynamoDBMapper.scan(GroupMember.class, scanExpression);
        return members.isEmpty() ? Optional.empty() : Optional.of(members.get(0));
    }
    
    public boolean existsByGroupIdAndUserId(String groupId, String userId) {
        return findByGroupIdAndUserId(groupId, userId).isPresent();
    }
    
    public List<GroupMember> findByGroupIdAndRole(String groupId, String role) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":groupId", new AttributeValue().withS(groupId));
        eav.put(":role", new AttributeValue().withS(role));
        
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("groupId = :groupId AND role = :role")
                .withExpressionAttributeValues(eav);
        
        return dynamoDBMapper.scan(GroupMember.class, scanExpression);
    }
    
    public void deleteByGroupIdAndUserId(String groupId, String userId) {
        Optional<GroupMember> member = findByGroupIdAndUserId(groupId, userId);
        if (member.isPresent()) {
            dynamoDBMapper.delete(member.get());
        }
    }
    
    public void deleteAll(List<GroupMember> members) {
        dynamoDBMapper.batchDelete(members);
    }
} 