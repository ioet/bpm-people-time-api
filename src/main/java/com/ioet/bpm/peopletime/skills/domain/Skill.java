package com.ioet.bpm.peopletime.skills.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Data
public class Skill {

    @NotBlank
    @DynamoDBAttribute
    private String id;

    @NotBlank
    @DynamoDBAttribute
    private String name;
}

