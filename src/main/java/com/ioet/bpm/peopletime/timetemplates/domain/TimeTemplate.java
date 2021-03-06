package com.ioet.bpm.peopletime.timetemplates.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ioet.bpm.peopletime.skills.domain.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dozer.Mapping;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@DynamoDBTable(tableName = "people_time_time_template")
public class TimeTemplate {

    @Mapping("templateId")
    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    @NotBlank
    @DynamoDBAttribute
    private String name;

    @NotBlank
    @DynamoDBAttribute
    private String personId;

    @NotBlank
    @DynamoDBAttribute
    private String organizationId;

    @NotBlank
    @DynamoDBAttribute
    private String organizationName;

    @NotBlank
    @DynamoDBAttribute
    private String projectId;

    @NotBlank
    @DynamoDBAttribute
    private String projectName;

    @NotBlank
    @DynamoDBAttribute
    private String activity;

    @DynamoDBTypeConvertedJson
    @DynamoDBAttribute
    private List<Skill> skills;
}
