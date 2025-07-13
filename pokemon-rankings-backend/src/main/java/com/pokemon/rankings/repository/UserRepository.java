package com.pokemon.rankings.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.pokemon.rankings.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
    
    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    
    public User save(User user) {
        dynamoDBMapper.save(user);
        return user;
    }
    
    public Optional<User> findById(String userId) {
        User user = dynamoDBMapper.load(User.class, userId);
        return Optional.ofNullable(user);
    }
    
    public Optional<User> findByUsername(String username) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":username", new AttributeValue().withS(username));
        
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("username = :username")
                .withExpressionAttributeValues(eav);
        
        List<User> users = dynamoDBMapper.scan(User.class, scanExpression);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
    
    public Optional<User> findByEmail(String email) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":email", new AttributeValue().withS(email));
        
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("email = :email")
                .withExpressionAttributeValues(eav);
        
        List<User> users = dynamoDBMapper.scan(User.class, scanExpression);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
    
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }
    
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
    
    public List<User> findByUsernameContainingIgnoreCase(String username) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":username", new AttributeValue().withS(username));
        
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("contains(username, :username)")
                .withExpressionAttributeValues(eav);
        
        return dynamoDBMapper.scan(User.class, scanExpression);
    }
} 