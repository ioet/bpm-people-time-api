package com.ioet.bpm.peopletime.timeevents.repositories;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.ioet.bpm.peopletime.timeevents.domain.TimeEvent;

import java.time.Instant;
import java.util.*;

public class CustomRepositoryImpl implements CustomRepository {

    @Override
    public Optional<TimeEvent> findLastActiveTimeEvent(String personId) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        String actualDate = (String.valueOf(Instant.now()));
        Map<String, String> attributeNames = new HashMap<>();
        attributeNames.put("#personId", "personId");
        attributeNames.put("#startTime", "startTime");
        Map<String, AttributeValue> attributeValues = new HashMap<>();
        attributeValues.put(":personId", new AttributeValue().withS(personId));
        attributeValues.put(":actualDate", new AttributeValue().withS(actualDate));

        DynamoDBQueryExpression dynamoDBQueryExpression = new DynamoDBQueryExpression()
                .withKeyConditionExpression("#personId = :personId and #startTime < :actualDate")
                .withConsistentRead(false)
                .withScanIndexForward(false)
                .withIndexName("personId-startTime-index")
                .withExpressionAttributeNames(attributeNames)
                .withExpressionAttributeValues(attributeValues);

        List<TimeEvent> timeEventsList = mapper.query(TimeEvent.class, dynamoDBQueryExpression);
        return Optional.of(timeEventsList.get(0));
    }
}
