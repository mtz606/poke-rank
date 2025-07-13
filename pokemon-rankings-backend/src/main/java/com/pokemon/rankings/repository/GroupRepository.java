package com.pokemon.rankings.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.pokemon.rankings.entity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GroupRepository {
    
    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    
    public Group save(Group group) {
        dynamoDBMapper.save(group);
        return group;
    }
    
    public Optional<Group> findById(String groupId) {
        Group group = dynamoDBMapper.load(Group.class, groupId);
        return Optional.ofNullable(group);
    }
    
    public List<Group> findByOwnerIdAndIsActiveTrue(String ownerId) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":ownerId", new AttributeValue().withS(ownerId));
        eav.put(":isActive", new AttributeValue().withBOOL(true));
        
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("ownerId = :ownerId AND isActive = :isActive")
                .withExpressionAttributeValues(eav);
        
        return dynamoDBMapper.scan(Group.class, scanExpression);
    }
    
    public Optional<Group> findByNameAndIsActiveTrue(String name) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":name", new AttributeValue().withS(name));
        eav.put(":isActive", new AttributeValue().withBOOL(true));
        
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("name = :name AND isActive = :isActive")
                .withExpressionAttributeValues(eav);
        
        List<Group> groups = dynamoDBMapper.scan(Group.class, scanExpression);
        return groups.isEmpty() ? Optional.empty() : Optional.of(groups.get(0));
    }
    
    public List<Group> findByIsActiveTrue() {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":isActive", new AttributeValue().withBOOL(true));
        
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("isActive = :isActive")
                .withExpressionAttributeValues(eav);
        
        return dynamoDBMapper.scan(Group.class, scanExpression);
    }
    
    public List<Group> findByNameContainingIgnoreCaseAndIsActiveTrue(String name) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":name", new AttributeValue().withS(name));
        eav.put(":isActive", new AttributeValue().withBOOL(true));
        
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("contains(name, :name) AND isActive = :isActive")
                .withExpressionAttributeValues(eav);
        
        return dynamoDBMapper.scan(Group.class, scanExpression);
    }
} 