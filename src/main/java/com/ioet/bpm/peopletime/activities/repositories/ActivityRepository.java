package com.ioet.bpm.peopletime.activities.repositories;

import com.ioet.bpm.peopletime.activities.domain.Activity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface ActivityRepository extends CrudRepository<Activity, String> {
}
