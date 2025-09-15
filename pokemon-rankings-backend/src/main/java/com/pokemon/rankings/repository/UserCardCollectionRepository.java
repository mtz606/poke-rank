package com.pokemon.rankings.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.pokemon.rankings.entity.UserCardCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserCardCollectionRepository {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public UserCardCollection save(UserCardCollection entry) {
        dynamoDBMapper.save(entry);
        return entry;
    }

    public List<UserCardCollection> findByUserId(String userId) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":userId", new AttributeValue().withS(userId));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("userId = :userId")
                .withExpressionAttributeValues(eav);
        return dynamoDBMapper.scan(UserCardCollection.class, scanExpression);
    }

    public void batchSave(List<UserCardCollection> entries) {
        dynamoDBMapper.batchSave(entries);
    }

    public void batchDelete(List<UserCardCollection> entries) {
        dynamoDBMapper.batchDelete(entries);
    }
} 