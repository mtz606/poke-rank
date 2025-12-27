package com.pokemon.rankings.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBConfig {

    @Value("${amazon.dynamodb.endpoint:}")
    private String dynamoDbEndpoint;

    @Value("${amazon.aws.accesskey:}")
    private String accessKey;

    @Value("${amazon.aws.secretkey:}")
    private String secretKey;

    @Value("${amazon.aws.region:us-east-1}")
    private String region;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard();
        
        // If endpoint is provided (local development), use it
        // Otherwise, use default AWS DynamoDB service (production)
        if (dynamoDbEndpoint != null && !dynamoDbEndpoint.isEmpty()) {
            builder.withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration(dynamoDbEndpoint, region)
            );
        } else {
            // For production, use default AWS DynamoDB endpoint
            builder.withRegion(region);
        }
        
        // Set credentials if provided
        // For local development: use provided credentials (even if "local")
        // For production with IAM roles: credentials are automatically provided, so we skip
        if (accessKey != null && !accessKey.isEmpty() && secretKey != null && !secretKey.isEmpty()) {
            builder.withCredentials(
                    new AWSStaticCredentialsProvider(
                            new BasicAWSCredentials(accessKey, secretKey)
                    )
            );
        }
        // If credentials are not provided, AWS SDK will use default credential chain
        // (IAM roles, environment variables, ~/.aws/credentials, etc.)
        
        AmazonDynamoDB dynamoDB = builder.build();

        // Create tables if they don't exist
        createTablesIfNotExist(dynamoDB);

        return dynamoDB;
    }
    
    @Bean
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB);
    }

    private void createTablesIfNotExist(AmazonDynamoDB dynamoDB) {
        // Create Users table
        createTableIfNotExists(dynamoDB, "Users", "userId");
        
        // Create Groups table
        createTableIfNotExists(dynamoDB, "Groups", "groupId");
        
        // Create GroupMembers table
        createGroupMembersTableIfNotExists(dynamoDB);
        
        // Create UserCardCollection table
        createUserCardCollectionTableIfNotExists(dynamoDB);
    }

    private void createTableIfNotExists(AmazonDynamoDB dynamoDB, String tableName, String hashKeyName) {
        try {
            CreateTableRequest createTableRequest = new CreateTableRequest()
                    .withTableName(tableName)
                    .withKeySchema(new KeySchemaElement(hashKeyName, KeyType.HASH))
                    .withAttributeDefinitions(new AttributeDefinition(hashKeyName, ScalarAttributeType.S))
                    .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

            dynamoDB.createTable(createTableRequest);
            System.out.println("Created table: " + tableName);
        } catch (ResourceInUseException e) {
            System.out.println("Table " + tableName + " already exists");
        }
    }

    private void createGroupMembersTableIfNotExists(AmazonDynamoDB dynamoDB) {
        try {
            CreateTableRequest createTableRequest = new CreateTableRequest()
                    .withTableName("GroupMembers")
                    .withKeySchema(
                            new KeySchemaElement("groupId", KeyType.HASH),
                            new KeySchemaElement("userId", KeyType.RANGE)
                    )
                    .withAttributeDefinitions(
                            new AttributeDefinition("groupId", ScalarAttributeType.S),
                            new AttributeDefinition("userId", ScalarAttributeType.S)
                    )
                    .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

            dynamoDB.createTable(createTableRequest);
            System.out.println("Created table: GroupMembers");
        } catch (ResourceInUseException e) {
            System.out.println("Table GroupMembers already exists");
        }
    }
    
    private void createUserCardCollectionTableIfNotExists(AmazonDynamoDB dynamoDB) {
        try {
            CreateTableRequest createTableRequest = new CreateTableRequest()
                    .withTableName("UserCardCollection")
                    .withKeySchema(
                            new KeySchemaElement("userId", KeyType.HASH),
                            new KeySchemaElement("cardId", KeyType.RANGE)
                    )
                    .withAttributeDefinitions(
                            new AttributeDefinition("userId", ScalarAttributeType.S),
                            new AttributeDefinition("cardId", ScalarAttributeType.S)
                    )
                    .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

            dynamoDB.createTable(createTableRequest);
            System.out.println("Created table: UserCardCollection");
        } catch (ResourceInUseException e) {
            System.out.println("Table UserCardCollection already exists");
        }
    }
} 